package com.example.spendoov2

import android.app.Application
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.example.spendoov2.ExportState
import com.example.spendoov2.data.LocalData
import com.example.spendoov2.TransactionData
import java.io.IOException

class ExportViewModel(application: Application) : AndroidViewModel(application) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // 1. Expose the UI state
    private val _uiState = MutableStateFlow<ExportState>(ExportState.Idle)
    val uiState = _uiState.asStateFlow()

    fun exportUserData() {
        if (_uiState.value == ExportState.Exporting) return
        _uiState.value = ExportState.Exporting

        val currentUser = auth.currentUser

        viewModelScope.launch {
            try {
                // We'll store the list of transactions here,
                // regardless of the source (Firestore or Local)
                val transactionsToExport: List<TransactionData>
                val filename: String

                if (currentUser != null) {
                    // --- MODE LOGIN: Fetch from Firestore ---
                    Log.d("ExportViewModel", "User logged in. Fetching from Firestore...")

                    val snapshot = db.collection("users")
                        .document(currentUser.uid)
                        .collection("transactions")
                        .get()
                        .await()

                    if (snapshot.isEmpty) {
                        _uiState.value = ExportState.Error("No data found to export")
                        return@launch
                    }

                    transactionsToExport = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(TransactionData::class.java)?.copy(id = doc.id)
                    }
                    filename = "transactions_${currentUser.uid}.csv" // CHANGED extension

                } else {
                    // --- MODE GUEST: Use local LocalData.TransactionLists ---
                    Log.d("ExportViewModel", "User is guest. Using local list.")

                    if (LocalData.TransactionLists.isEmpty()) {
                        _uiState.value = ExportState.Error("No local data to export")
                        return@launch
                    }

                    transactionsToExport = LocalData.TransactionLists.toList()
                    filename = "transactions_local_guest.csv" // CHANGED extension
                }

                // --- 1. Convert the list to a CSV String ---
                val csvContent = convertToCsv(transactionsToExport) // NEW function call

                // --- 2. Save File (with new MIME type) ---
                saveContentToDownloads(filename, csvContent, "text/csv") // UPDATED function call

                _uiState.value = ExportState.Success

            } catch (e: Exception) {
                Log.e("ExportViewModel", "Export failed", e)
                _uiState.value = ExportState.Error(e.message)
            }
        }
    }

    /**
     * NEW: Converts a list of TransactionData into a single CSV-formatted String.
     */
    private fun convertToCsv(transactions: List<TransactionData>): String {
        val stringBuilder = StringBuilder()

        // 1. Append Header Row
        // We skip 'image' as it's a resource ID, not useful in a CSV
        stringBuilder.append("id,type,category,date,month,year,amount\n")

        // 2. Append Data Rows
        transactions.forEach { t ->
            // Basic CSV escaping:
            // - "escape" quotes by doubling them ("" becomes """")
            // - wrap any value with a comma or quote in quotes
            val id = escapeCsvValue(t.id)
            val type = escapeCsvValue(t.type)
            val category = escapeCsvValue(t.category)

            stringBuilder.append(
                "$id,$type,$category,${t.date},${t.month},${t.year},${t.amount}\n"
            )
        }
        return stringBuilder.toString()
    }

    /**
     * Helper for robust CSV conversion.
     */
    private fun escapeCsvValue(value: String?): String {
        val stringValue = value ?: ""
        // If the value contains a comma, newline, or quote, wrap it in double quotes
        if (stringValue.contains(",") || stringValue.contains("\"") || stringValue.contains("\n")) {
            // Inside the quotes, any existing quotes must be doubled
            return "\"${stringValue.replace("\"", "\"\"")}\""
        }
        return stringValue
    }

    /**
     * UPDATED: Renamed from saveJsonToDownloads to be more generic.
     * Now accepts a MIME type.
     */
    private suspend fun saveContentToDownloads(
        filename: String,
        content: String,
        mimeType: String // NEW parameter
    ) {
        withContext(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            val resolver = context.contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType) // Use the parameter
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri == null) {
                throw IOException("Failed to create new MediaStore entry")
            }

            resolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(content.toByteArray())
            }
        }
    }

    fun resetState() {
        _uiState.value = ExportState.Idle
    }
}