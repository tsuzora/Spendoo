package com.example.spendoov2

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.spendoov2.data.LocalData
import com.example.spendoov2.ui.theme.MainBackgroundColor
import com.example.spendoov2.ui.theme.interFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit,
    exportViewModel: ExportViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    val exportUiState by exportViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showExport by remember { mutableStateOf(false) }
    var showLogout by remember { mutableStateOf(false) }
    var showChangeUsername by remember { mutableStateOf(false) }
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MainBackgroundColor)
            .padding(24.dp, 35.dp)
    ) {
        // 1. Tombol Kembali
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_side_line_left),
                contentDescription = "Kembali",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Judul
        Text(
            text = "Profile Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFamily,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Tombol-tombol yang diperbarui

        // 2. Tombol Ganti Username
        ProfileButton(
            text = "Change Username",
            icon = null
            // <-- DIPERBAIKI: Parameter 'icon' dihapus sesuai permintaan
        ) {
            showChangeUsername = true
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Tombol Ganti Password
        ProfileButton(
            text = "Change Password",
            painter = painterResource(R.drawable.lock_icon),
            contentDescription = "Change Password"
            // <-- Panggilan ini sekarang valid karena ada overload ProfileButton (painter)
        ) {
            navController.navigate("forgot_password_screen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Tombol Export Transactions
        ProfileButton(
            text = "Export Transactions",
            painter = painterResource(R.drawable.download_icon), // Ikon baru
            contentDescription = "Export Transactions"
        ) {
            showExport = true
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Tombol Logout (dengan style destruktif)
        val destructiveColor = Color(0xFFE57373) // Warna merah untuk logout
        ProfileButton(
            text = "Logout",
            painter = painterResource(R.drawable.logout_icon), // Ikon baru
            contentDescription = "Logout",
            contentColor = destructiveColor // Terapkan warna destruktif
        ) {
            showLogout = true
        }
    }

    // Overlay untuk Export
    if (showExport) {
        ExportOptionsOverlay(
            onDismiss = { showExport = false },
            onAllTransactionsClick = {
                showExport = false
                exportViewModel.exportUserData()
            },
            onDailyClick = {
                showExport = false
                Toast.makeText(context, "Daily export coming soon!", Toast.LENGTH_SHORT).show()
            },
            onMonthlyClick = {
                showExport = false
                Toast.makeText(context, "Daily export coming soon!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Overlay untuk Konfirmasi Logout
    if (showLogout) {
        ConfirmationOverlay(
            message = "Are you sure you want to logout?",
            onConfirm = {
                showLogout = false
                auth.signOut()
                onLogout()
            },
            onCancel = { showLogout = false }
        )
    }

    if (showChangeUsername) {
        ChangeUsernameOverlay(
            currentUsername = auth.currentUser?.displayName ?: "",
            onDismiss = { showChangeUsername = false },
            onSave = { newName ->
                val user = auth.currentUser
                if (user != null && newName.isNotBlank()) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build()

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("ProfileScreen", "User profile updated successfully.")
                                // Opsional: Tampilkan Toast atau Snackbar
                            } else {
                                Log.w("ProfileScreen", "Error updating profile.", task.exception)
                                // Opsional: Tampilkan error
                            }
                        }
                } else {
                    LocalData.Name = newName
                }
                showChangeUsername = false
            }
        )
    }
}

@Composable
fun ResetPasswordScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MainBackgroundColor)
            .padding(24.dp, 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tombol Kembali
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_side_line_left),
                contentDescription = "Kembali",
                colorFilter = ColorFilter.tint(Color.White), // Tint icon ke putih
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Reset Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFamily,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "UI untuk reset password akan ada di sini.",
            fontSize = 16.sp,
            fontFamily = interFamily,
            color = Color.White
        )
        // TODO: Tambahkan UI untuk reset password di sini (Misal: TextField untuk email, Button kirim)
    }
}

@Composable
fun ProfileButton(
    text: String,
    icon: ImageVector? = null,
    painter: Painter? = null, // <-- Parameter untuk Painter
    contentDescription: String? = null, // <-- Parameter deskripsi untuk Painter
    backgroundColor: Color = Color.White.copy(alpha = 0.1f),
    contentColor: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        // Ikon di sebelah kiri (menggunakan Painter)
        if (painter != null) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp)) // Jarak antara ikon dan teks

        // Teks
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = interFamily,
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
            modifier = Modifier.weight(1f)
        )

        // Ikon panah di sebelah kanan
        Icon(
            painter = painterResource(R.drawable.arrow_side_line_right),
            contentDescription = null,
            tint = contentColor.copy(alpha = 0.7f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun ChangeUsernameOverlay(
    currentUsername: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newUsername by remember { mutableStateOf(TextFieldValue(currentUsername)) }

    // Menggunakan Dialog untuk overlay sederhana
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray) // Sesuaikan warnanya
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Change Username",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = interFamily
                )

                // TextField untuk username baru
                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("New Username") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Tombol Aksi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Batal
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Tombol Simpan
                    Button(
                        onClick = { onSave(newUsername.text) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Save", color = Color.Black)
                    }
                }
            }
        }
    }
}