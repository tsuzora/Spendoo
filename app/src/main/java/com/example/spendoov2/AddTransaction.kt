

package com.example.spendoov2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

// Definisikan warna di sini agar mudah diakses
val AddIncomeBGColor = Color(0xFF54E871)
val AddExpenseBGColor = Color(0xFFFF6A6A)
val UnselectedColor = Color(0xFFACACAC)

@Composable
fun AddTransaction(
    navController: NavController,
    transactionId: String? = null,
    modifier: Modifier = Modifier
) {
    // Cari transaksi berdasarkan ID jika dalam mode edit
    val transactionToEdit = transactionId?.let { id ->
        TransactionLists.find { it.id == id && id != "new" }
    }
    val isEditMode = transactionToEdit != null

    // State untuk input pengguna
    var selectedType by remember { mutableStateOf(transactionToEdit?.type?.replaceFirstChar { it.uppercase() } ?: "Expense") }
    var amount by remember { mutableStateOf(transactionToEdit?.amount?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf<Pair<String, Int>?>(
        if (isEditMode) Pair(transactionToEdit!!.category, transactionToEdit.image) else null
    ) }
    // Inisialisasi payment method dengan nilai default jika dalam mode edit, karena data tidak tersimpan
    var selectedPaymentMethod by remember { mutableStateOf<Pair<String, Int>?>(if(isEditMode) "Cash" to R.drawable.cash_icon else null) }

    // State untuk tanggal dan waktu
    val initialCalendar = Calendar.getInstance().apply {
        if (isEditMode && transactionToEdit != null) {
            set(Calendar.YEAR, transactionToEdit.year)
            val monthInt = SimpleDateFormat("MMMM", Locale.ENGLISH).parse(transactionToEdit.month)?.let {
                Calendar.getInstance().apply { time = it }.get(Calendar.MONTH)
            } ?: get(Calendar.MONTH)
            set(Calendar.MONTH, monthInt)
            set(Calendar.DAY_OF_MONTH, transactionToEdit.date)
        }
    }
    var selectedDate by remember { mutableStateOf(initialCalendar) }
    var selectedHour by remember { mutableStateOf(12) }
    var selectedMinute by remember { mutableStateOf(0) }
    var selectedIsAm by remember { mutableStateOf(false) } // false for PM, true for AM

    // State untuk visibility overlay
    var showCategoryOverlay by remember { mutableStateOf(false) }
    var showPaymentMethodOverlay by remember { mutableStateOf(false) }
    var showTimePickerOverlay by remember { mutableStateOf(false) }
    var showCalendarOverlay by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.US) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF178237), Color(0xFF1B1C28)),
                    startY = 0f,
                    endY = 800f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp)
        ) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_side_line_left),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = if (isEditMode) "Edit Transaction" else "Add Transaction",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Toggle Income / Expense
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                TransactionTypeButton(
                    "Income",
                    selectedType == "Income",
                    {
                        if (selectedType != "Income") {
                            selectedType = "Income"
                            selectedCategory = null
                        }
                    },
                    Modifier.weight(1f))
                TransactionTypeButton(
                    "Expense",
                    selectedType == "Expense",
                    {
                        if (selectedType != "Expense") {
                            selectedType = "Expense"
                            selectedCategory = null
                        }
                    },
                    Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Tanggal dan Waktu
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                DateDisplay(
                    date = dateFormat.format(selectedDate.time),
                    onClick = { showCalendarOverlay = true }
                )
                TimeDisplay(
                    hour = selectedHour,
                    minute = selectedMinute,
                    isAm = selectedIsAm,
                    onClick = { showTimePickerOverlay = true }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Form Fields
            AmountField(value = amount, onValueChange = { amount = it })
            Spacer(modifier = Modifier.height(20.dp))
            TransactionField(
                iconRes = selectedCategory?.second,
                value = selectedCategory?.first ?: "Category",
                onClick = { showCategoryOverlay = true }
            )
            Spacer(modifier = Modifier.height(20.dp))
            TransactionField(
                iconRes = selectedPaymentMethod?.second,
                value = selectedPaymentMethod?.first ?: "Payment Method",
                onClick = { showPaymentMethodOverlay = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Aksi
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                if (isEditMode) {
                    ActionButton(
                        text = "Delete",
                        iconRes = R.drawable.delete_icon,
                        backgroundColor = Color(0xFFFF6A6A),
                        onClick = { showDeleteConfirmation = true }
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                }
                ActionButton(
                    text = "Save",
                    iconRes = R.drawable.save_icon,
                    backgroundColor = Color(0xFF54E871),
                    onClick = {
                        val finalAmount = amount.toIntOrNull()
                        // Validasi: Amount, Kategori, dan Metode Pembayaran harus diisi
                        if (finalAmount != null && selectedCategory != null && selectedPaymentMethod != null) {
                            val newOrUpdatedTransaction = TransactionData(
                                id = transactionToEdit?.id ?: UUID.randomUUID().toString(),
                                type = selectedType.lowercase(),
                                category = selectedCategory!!.first,
                                date = selectedDate.get(Calendar.DAY_OF_MONTH),
                                month = selectedDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) ?: "",
                                year = selectedDate.get(Calendar.YEAR),
                                image = selectedCategory!!.second,
                                amount = finalAmount
                            )

                            if (isEditMode && transactionToEdit != null) {
                                // Update transaksi
                                val index = TransactionLists.indexOfFirst { it.id == transactionToEdit.id }
                                if (index != -1) {
                                    TransactionLists[index] = newOrUpdatedTransaction
                                }
                            } else {
                                // Tambah transaksi baru
                                TransactionLists.add(0, newOrUpdatedTransaction)
                            }

                            navController.popBackStack()
                        }
                    }
                )
            }
        }

        // Overlays
        if (showCategoryOverlay) {
            CategorySelectionOverlay(
                transactionType = selectedType,
                onDismiss = { showCategoryOverlay = false },
                onCategorySelected = { categoryName, iconRes ->
                    selectedCategory = Pair(categoryName, iconRes)
                    showCategoryOverlay = false
                }
            )
        }
        if (showPaymentMethodOverlay) {
            PaymentMethodOverlay(
                onDismiss = { showPaymentMethodOverlay = false },
                onPaymentMethodSelected = { methodName, iconRes ->
                    selectedPaymentMethod = Pair(methodName, iconRes)
                    showPaymentMethodOverlay = false
                }
            )
        }
        if (showTimePickerOverlay) {
            TimePickerOverlay(
                initialHour = selectedHour,
                initialMinute = selectedMinute,
                initialIsAm = selectedIsAm,
                onDismiss = { showTimePickerOverlay = false },
                onTimeSelected = { hour, minute, isAm ->
                    selectedHour = hour
                    selectedMinute = minute
                    selectedIsAm = isAm
                    showTimePickerOverlay = false
                }
            )
        }
        if (showCalendarOverlay) {
            CalendarOverlay(
                initialDate = selectedDate,
                onDismiss = { showCalendarOverlay = false },
                onDateSelected = { newDate ->
                    selectedDate = newDate
                    showCalendarOverlay = false
                }
            )
        }
        if (showDeleteConfirmation) {
            ConfirmationOverlay(
                message = "Are you sure you want to delete this transaction?",
                onConfirm = {
                    transactionToEdit?.let { TransactionLists.remove(it) }
                    showDeleteConfirmation = false
                    navController.popBackStack()
                },
                onCancel = { showDeleteConfirmation = false }
            )
        }
    }
}

// --- Composable-composable Helper ---

@Composable
fun TransactionTypeButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val backgroundColor = when {
        isSelected && text == "Income" -> AddIncomeBGColor
        isSelected && text == "Expense" -> AddExpenseBGColor
        else -> UnselectedColor
    }

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        modifier = modifier.fillMaxSize(),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
    ) {
        Text(text = text, fontSize = 14.sp)
    }
}

@Composable
fun DateDisplay(date: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = R.drawable.calendar_icon),
            contentDescription = "calendar_icon",
            modifier = Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = date, color = Color.White, style = TextStyle(fontSize = 14.sp))
    }
}

@Composable
fun TimeDisplay(hour: Int, minute: Int, isAm: Boolean, onClick: () -> Unit) {
    val period = if (isAm) "AM" else "PM"
    // Konversi format 24 jam ke 12 jam untuk ditampilkan
    val formattedHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val formattedMinute = String.format("%02d", minute)
    val timeString = "$formattedHour:$formattedMinute $period"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = R.drawable.clock_icon),
            contentDescription = "clock_icon",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(30.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = timeString, color = Color.White, style = TextStyle(fontSize = 14.sp))
    }
}

@Composable
fun AmountField(value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Rp",
                color = Color.White.copy(alpha = 0.7f),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 8.dp)
            )
            BasicTextField(
                value = value,
                onValueChange = {
                        newText ->
                    if (newText.all { it.isDigit() }) {
                        onValueChange(newText)
                    }
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Amount",
                            style = TextStyle(
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Light
                            )
                        )
                    }
                    innerTextField()
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.White.copy(alpha = 0.5f))
    }
}

@Composable
fun TransactionField(iconRes: Int?, value: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconRes != null) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = value,
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            } else {
                // Placeholder agar layout tidak bergeser
                Box(Modifier.size(30.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = value,
                color = if (value == "Category" || value == "Payment Method") Color.White.copy(alpha = 0.5f) else Color.White,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Light),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.White.copy(alpha = 0.5f))
    }
}

@Composable
fun ActionButton(text: String, iconRes: Int, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = Modifier.height(50.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

// --- Overlay Composables ---

@Composable
fun CategorySelectionOverlay(
    transactionType: String,
    onDismiss: () -> Unit,
    onCategorySelected: (name: String, iconRes: Int) -> Unit
) {
    val categories = if (transactionType.equals("Income", ignoreCase = true)) categoryIncome else categoryExpense
    val gradient = if (transactionType.equals("Income", ignoreCase = true)) {
        Brush.linearGradient(listOf(Color(0xFF2f9944), Color(0xFF54E871)))
    } else {
        Brush.linearGradient(listOf(Color(0xFF994040), Color(0xFFFF6A6A)))
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(24.dp))
                .background(brush = gradient)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Category $transactionType",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.icone_close),
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = onDismiss)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories.entries.toList()) { (name, iconRes) ->
                        CategoryItem(
                            name = name,
                            iconRes = iconRes,
                            onClick = { onCategorySelected(name, iconRes) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentMethodOverlay(onDismiss: () -> Unit, onPaymentMethodSelected: (name: String, iconRes: Int) -> Unit) {
    val paymentMethods = listOf(
        "Cash" to R.drawable.cash_icon,
        "Debit Card" to R.drawable.credit_card, // Nama file disesuaikan menjadi credit_card
        "E-Wallet" to R.drawable.ewallet_icon,
        "Others" to R.drawable.others
    )
    val gradient = Brush.linearGradient(listOf(Color(0xFF2f9944), Color(0xFF54E871)))

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(24.dp))
                .background(brush = gradient)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Payment Method", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Icon(
                        painter = painterResource(id = R.drawable.icone_close),
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = onDismiss)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(paymentMethods) { (name, iconRes) ->
                        CategoryItem(
                            name = name,
                            iconRes = iconRes,
                            onClick = { onPaymentMethodSelected(name, iconRes) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimePickerOverlay(
    initialHour: Int, initialMinute: Int, initialIsAm: Boolean,
    onDismiss: () -> Unit, onTimeSelected: (hour: Int, minute: Int, isAm: Boolean) -> Unit
) {
    // Internal state for the picker, using a 12-hour format for the UI
    var hour by remember { mutableStateOf(if (initialHour > 12) initialHour - 12 else if (initialHour == 0) 12 else initialHour) }
    var minute by remember { mutableStateOf(initialMinute) }
    var isAm by remember { mutableStateOf(initialIsAm) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Time Picker", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Main row containing numbers and AM/PM toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                NumberPicker(value = hour, onValueChange = { hour = it }, range = 1..12)
                Text(":", fontSize = 48.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                NumberPicker(value = minute, onValueChange = { minute = it }, range = 0..59)
                Spacer(Modifier.width(16.dp))

                // AM/PM buttons stacked vertically
                Column {
                    val amBgColor = if (isAm) AddIncomeBGColor else Color.LightGray.copy(alpha = 0.3f)
                    val pmBgColor = if (!isAm) AddIncomeBGColor else Color.LightGray.copy(alpha = 0.3f)
                    val amTextColor = if (isAm) Color.White else Color.Black
                    val pmTextColor = if (!isAm) Color.White else Color.Black

                    TextButton(
                        onClick = { isAm = true },
                        modifier = Modifier.background(amBgColor, RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.textButtonColors(contentColor = amTextColor)
                    ) { Text("AM") }

                    Spacer(modifier = Modifier.height(4.dp))

                    TextButton(
                        onClick = { isAm = false },
                        modifier = Modifier.background(pmBgColor, RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.textButtonColors(contentColor = pmTextColor)
                    ) { Text("PM") }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm button to set the time and close the dialog
            Button(
                onClick = {
                    // Convert 12-hour format back to 24-hour format before saving
                    val finalHour = when {
                        isAm && hour == 12 -> 0    // 12 AM is 00:00
                        !isAm && hour < 12 -> hour + 12 // 1 PM to 11 PM
                        else -> hour                   // Handles 1 AM-11 AM and 12 PM
                    }
                    onTimeSelected(finalHour, minute, isAm)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AddIncomeBGColor)
            ) {
                Text("Set Time", color = Color.White)
            }
        }
    }
}

@Composable
fun NumberPicker(value: Int, onValueChange: (Int) -> Unit, range: IntRange) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_up),
            contentDescription = "Increase",
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    onValueChange((value + 1).let { if (it > range.last) range.first else it })
                }
        )
        Text(String.format("%02d", value), fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Icon(
            painter = painterResource(id = R.drawable.arrow_down),
            contentDescription = "Decrease",
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    onValueChange((value - 1).let { if (it < range.first) range.last else it })
                }
        )
    }
}

@Composable
fun CalendarOverlay(initialDate: Calendar, onDismiss: () -> Unit, onDateSelected: (Calendar) -> Unit) {
    var displayedMonth by remember { mutableStateOf(initialDate.clone() as Calendar) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_side_line_left),
                    contentDescription = "Previous Month",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            displayedMonth = (displayedMonth.clone() as Calendar).apply {
                                add(
                                    Calendar.MONTH,
                                    -1
                                )
                            }
                        }
                )
                Text(
                    text = SimpleDateFormat("MMMM yyyy", Locale.US).format(displayedMonth.time),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.arrow_side_line_right),
                    contentDescription = "Next Month",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            displayedMonth = (displayedMonth.clone() as Calendar).apply {
                                add(
                                    Calendar.MONTH,
                                    1
                                )
                            }
                        }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // Day of Week Headers
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                val days = listOf("SEN", "SEL", "RAB", "KAM", "JUM", "SAB", "MIN")
                days.forEach { Text(it, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray) }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Dates
            val daysInMonth = displayedMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            val firstDayOfMonth = (displayedMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK)
            val offset = (firstDayOfMonth - Calendar.MONDAY + 7) % 7 // Adjusted for SEN as first day

            LazyVerticalGrid(columns = GridCells.Fixed(7)) {
                items(offset) { Spacer(modifier = Modifier.size(40.dp)) }
                items(daysInMonth) { day ->
                    val date = (displayedMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, day + 1) }
                    val isSelected = day + 1 == initialDate.get(Calendar.DAY_OF_MONTH) &&
                            displayedMonth.get(Calendar.MONTH) == initialDate.get(Calendar.MONTH) &&
                            displayedMonth.get(Calendar.YEAR) == initialDate.get(Calendar.YEAR)

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) AddIncomeBGColor else Color.Transparent)
                            .clickable { onDateSelected(date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${day + 1}",
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(name: String, iconRes: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = name,
            modifier = Modifier.size(40.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true, name = "Add New Transaction")
@Composable
fun AddTransactionPreview() {
    val navController = rememberNavController()
    Surface {
        AddTransaction(navController = navController, transactionId = "new")
    }
}

// VERSION 3

//package com.example.spendoov2
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Locale
//import java.util.UUID
//
//// Definisikan warna di sini agar mudah diakses
//val AddIncomeBGColor = Color(0xFF54E871)
//val AddExpenseBGColor = Color(0xFFFF6A6A)
//val UnselectedColor = Color(0xFFACACAC)
//
//@Composable
//fun AddTransaction(
//    navController: NavController,
//    transactionId: String? = null,
//    modifier: Modifier = Modifier
//) {
//    // Cari transaksi yang akan diedit berdasarkan ID
//    val transactionToEdit = transactionId?.let { id ->
//        TransactionLists.find { it.id == id }
//    }
//
//    val isEditMode = transactionToEdit != null
//
//    // State untuk input pengguna, diinisialisasi dengan data yang ada jika dalam mode edit
//    var selectedType by remember { mutableStateOf(transactionToEdit?.type?.replaceFirstChar { it.uppercase() } ?: "Expense") }
//    var amount by remember { mutableStateOf(transactionToEdit?.amount?.toString() ?: "") }
//    var selectedCategory by remember {
//        mutableStateOf(
//            if (isEditMode) Pair(transactionToEdit!!.category, transactionToEdit.image) else null
//        )
//    }
//
//    // State untuk tanggal, diinisialisasi dengan data yang ada jika dalam mode edit
//    val initialCalendar = Calendar.getInstance().apply {
//        if (isEditMode && transactionToEdit != null) {
//            set(Calendar.YEAR, transactionToEdit.year)
//            val monthInt = SimpleDateFormat("MMMM", Locale.ENGLISH).parse(transactionToEdit.month)?.let {
//                Calendar.getInstance().apply { time = it }.get(Calendar.MONTH)
//            } ?: get(Calendar.MONTH)
//            set(Calendar.MONTH, monthInt)
//            set(Calendar.DAY_OF_MONTH, transactionToEdit.date)
//        }
//    }
//    var selectedDate by remember { mutableStateOf(initialCalendar) }
//
//    // State untuk visibility overlay
//    var showCategoryOverlay by remember { mutableStateOf(false) }
//    var showCalendarOverlay by remember { mutableStateOf(false) }
//    var showDeleteConfirmation by remember { mutableStateOf(false) }
//
//
//    val dateFormat = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.US) }
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(Color(0xFF178237), Color(0xFF1B1C28)),
//                    startY = 0f,
//                    endY = 800f
//                )
//            )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(26.dp)
//        ) {
//            // Top Bar
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.arrow_side_line_left),
//                    contentDescription = "Back",
//                    modifier = Modifier
//                        .size(25.dp)
//                        .clickable { navController.popBackStack() }
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(
//                    text = if (isEditMode) "Edit Transaction" else "Add Transaction",
//                    color = Color.White,
//                    style = MaterialTheme.typography.headlineSmall,
//                )
//            }
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            // Toggle Income / Expense
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(40.dp)
//                    .clip(RoundedCornerShape(20.dp))
//            ) {
//                TransactionTypeButton("Income", selectedType == "Income", { selectedType = "Income" }, Modifier.weight(1f))
//                TransactionTypeButton("Expense", selectedType == "Expense", { selectedType = "Expense" }, Modifier.weight(1f))
//            }
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            // Tampilan Tanggal
//            DateDisplay(
//                date = dateFormat.format(selectedDate.time),
//                onClick = { showCalendarOverlay = true }
//            )
//
//            Spacer(modifier = Modifier.height(40.dp))
//
//            // Form Fields
//            AmountField(value = amount, onValueChange = { amount = it })
//            Spacer(modifier = Modifier.height(20.dp))
//            TransactionField(
//                iconRes = selectedCategory?.second,
//                value = selectedCategory?.first ?: "Select Category",
//                onClick = { showCategoryOverlay = true }
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // Tombol Aksi
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 20.dp)
//            ) {
//                if (isEditMode) {
//                    ActionButton(
//                        text = "Delete",
//                        iconRes = R.drawable.delete_icon,
//                        backgroundColor = Color(0xFFFF6A6A),
//                        onClick = { showDeleteConfirmation = true }
//                    )
//                    Spacer(modifier = Modifier.width(20.dp))
//                }
//                ActionButton(
//                    text = "Save",
//                    iconRes = R.drawable.save_icon,
//                    backgroundColor = Color(0xFF54E871),
//                    onClick = {
//                        val finalAmount = amount.toIntOrNull()
//                        if (finalAmount != null && selectedCategory != null) {
//                            val newOrUpdatedTransaction = TransactionData(
//                                id = transactionToEdit?.id ?: UUID.randomUUID().toString(),
//                                type = selectedType.lowercase(),
//                                category = selectedCategory!!.first,
//                                date = selectedDate.get(Calendar.DAY_OF_MONTH),
//                                month = selectedDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) ?: "",
//                                year = selectedDate.get(Calendar.YEAR),
//                                image = selectedCategory!!.second,
//                                amount = finalAmount
//                            )
//
//                            if (isEditMode && transactionToEdit != null) {
//                                // Update transaksi yang ada
//                                val index = TransactionLists.indexOfFirst { it.id == transactionToEdit.id }
//                                if (index != -1) {
//                                    TransactionLists[index] = newOrUpdatedTransaction
//                                }
//                            } else {
//                                // Tambah transaksi baru
//                                TransactionLists.add(0, newOrUpdatedTransaction)
//                            }
//
//                            navController.popBackStack()
//                        }
//                    }
//                )
//            }
//        }
//
//        // Overlays
//        if (showCategoryOverlay) {
//            CategorySelectionOverlay(
//                transactionType = selectedType,
//                onDismiss = { showCategoryOverlay = false },
//                onCategorySelected = { categoryName, iconRes ->
//                    selectedCategory = Pair(categoryName, iconRes)
//                    showCategoryOverlay = false
//                }
//            )
//        }
//        if (showCalendarOverlay) {
//            CalendarOverlay(
//                initialDate = selectedDate,
//                onDismiss = { showCalendarOverlay = false },
//                onDateSelected = { newDate ->
//                    selectedDate = newDate
//                    showCalendarOverlay = false
//                }
//            )
//        }
//        if (showDeleteConfirmation) {
//            ConfirmationOverlay(
//                message = "Are you sure you want to delete this transaction?",
//                onConfirm = {
//                    transactionToEdit?.let {
//                        TransactionLists.remove(it)
//                    }
//                    showDeleteConfirmation = false
//                    navController.popBackStack()
//                },
//                onCancel = { showDeleteConfirmation = false }
//            )
//        }
//    }
//}
//
//
//@Composable
//fun TransactionField(iconRes: Int?, value: String, onClick: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            if (iconRes != null) {
//                Image(
//                    painter = painterResource(id = iconRes),
//                    contentDescription = value,
//                    modifier = Modifier.size(30.dp),
//                    colorFilter = ColorFilter.tint(Color.White)
//                )
//                Spacer(modifier = Modifier.width(12.dp))
//            } else {
//                // Placeholder icon if none is selected
//                Box(Modifier.size(30.dp))
//            }
//            Text(
//                text = value,
//                color = Color.White,
//                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Light),
//                modifier = Modifier.weight(1f)
//            )
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Divider(color = Color.White.copy(alpha = 0.5f))
//    }
//}
//
//@Composable
//fun TransactionTypeButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
//    val backgroundColor = when {
//        isSelected && text == "Income" -> AddIncomeBGColor
//        isSelected && text == "Expense" -> AddExpenseBGColor
//        else -> UnselectedColor
//    }
//
//    Button(
//        onClick = onClick,
//        shape = RoundedCornerShape(20.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = backgroundColor,
//            contentColor = if (isSelected) Color.White else Color.Black
//        ),
//        modifier = modifier.fillMaxSize(),
//        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
//    ) {
//        Text(text = text, fontSize = 14.sp)
//    }
//}
//
//@Composable
//fun DateDisplay(date: String, onClick: () -> Unit) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.clickable(onClick = onClick)
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.calendar_icon),
//            contentDescription = "calendar_icon",
//            modifier = Modifier.size(30.dp),
//            colorFilter = ColorFilter.tint(Color.White)
//        )
//        Spacer(Modifier.width(8.dp))
//        Text(text = date, color = Color.White, style = TextStyle(fontSize = 14.sp))
//    }
//}
//
//@Composable
//fun AmountField(value: String, onValueChange: (String) -> Unit) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = "Rp",
//                color = Color.White.copy(alpha = 0.7f),
//                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
//                modifier = Modifier.padding(end = 8.dp)
//            )
//            BasicTextField(
//                value = value,
//                onValueChange = onValueChange,
//                textStyle = TextStyle(
//                    color = Color.White,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.End
//                ),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                singleLine = true,
//                modifier = Modifier.weight(1f)
//            )
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Divider(color = Color.White.copy(alpha = 0.5f))
//    }
//}
//
//@Composable
//fun ActionButton(text: String, iconRes: Int, backgroundColor: Color, onClick: () -> Unit) {
//    Button(
//        onClick = onClick,
//        shape = RoundedCornerShape(16.dp),
//        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
//        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
//        modifier = Modifier.height(50.dp)
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                painter = painterResource(id = iconRes),
//                contentDescription = text,
//                tint = Color.White,
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
//        }
//    }
//}
//
//@Composable
//fun CategorySelectionOverlay(
//    transactionType: String,
//    onDismiss: () -> Unit,
//    onCategorySelected: (name: String, iconRes: Int) -> Unit
//) {
//    val categories = if (transactionType.equals("Income", ignoreCase = true)) categoryIncome else categoryExpense
//    val gradient = if (transactionType.equals("Income", ignoreCase = true)) {
//        Brush.linearGradient(listOf(Color(0xFF2f9944), Color(0xFF54E871)))
//    } else {
//        Brush.linearGradient(listOf(Color(0xFFFF6A6A), Color(0xFF994040)))
//    }
//
//    Dialog(onDismissRequest = onDismiss) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(0.95f)
//                .clip(RoundedCornerShape(24.dp))
//                .background(brush = gradient)
//                .padding(16.dp)
//        ) {
//            Column {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Category $transactionType",
//                        color = Color.White,
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Icon(
//                        painter = painterResource(id = R.drawable.icone_close),
//                        contentDescription = "Close",
//                        tint = Color.White,
//                        modifier = Modifier
//                            .size(24.dp)
//                            .clickable(onClick = onDismiss)
//                    )
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(3),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(categories.entries.toList()) { (name, iconRes) ->
//                        CategoryItem(
//                            name = name,
//                            iconRes = iconRes,
//                            onClick = { onCategorySelected(name, iconRes) }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CategoryItem(name: String, iconRes: Int, onClick: () -> Unit) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .clickable(onClick = onClick)
//            .padding(8.dp)
//    ) {
//        Image(
//            painter = painterResource(id = iconRes),
//            contentDescription = name,
//            modifier = Modifier.size(40.dp),
//            colorFilter = ColorFilter.tint(Color.White)
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = name,
//            color = Color.White,
//            textAlign = TextAlign.Center,
//            fontSize = 12.sp
//        )
//    }
//}
//
//
//@Composable
//fun CalendarOverlay(initialDate: Calendar, onDismiss: () -> Unit, onDateSelected: (Calendar) -> Unit) {
//    var displayedMonth by remember { mutableStateOf(initialDate.clone() as Calendar) }
//
//    Dialog(onDismissRequest = onDismiss) {
//        Column(
//            modifier = Modifier
//                .clip(RoundedCornerShape(24.dp))
//                .background(Color.White)
//                .padding(16.dp)
//        ) {
//            // Header: Month and Year Navigation
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.arrow_side_line_left),
//                    contentDescription = "Previous Month",
//                    modifier = Modifier
//                        .size(24.dp)
//                        .clickable {
//                            displayedMonth =
//                                (displayedMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
//                        }
//                )
//                Text(
//                    text = SimpleDateFormat("MMMM yyyy", Locale.US).format(displayedMonth.time),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//                Icon(
//                    painter = painterResource(id = R.drawable.arrow_side_line_right),
//                    contentDescription = "Next Month",
//                    modifier = Modifier
//                        .size(24.dp)
//                        .clickable {
//                            displayedMonth =
//                                (displayedMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
//                        }
//                )
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//            Divider()
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Day of Week Headers
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
//                val days = listOf("SEN", "SEL", "RAB", "KAM", "JUM", "SAB", "MIN")
//                days.forEach { Text(it, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray) }
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Calendar Dates
//            val daysInMonth = displayedMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
//            val firstDayOfMonth = (displayedMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK)
//            val offset = (firstDayOfMonth - Calendar.MONDAY + 7) % 7
//
//            LazyVerticalGrid(columns = GridCells.Fixed(7)) {
//                items(offset) { Spacer(modifier = Modifier.size(40.dp)) } // Empty spacers for offset
//                items(daysInMonth) { day ->
//                    val date = (displayedMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, day + 1) }
//                    val isSelected = day + 1 == initialDate.get(Calendar.DAY_OF_MONTH) &&
//                            displayedMonth.get(Calendar.MONTH) == initialDate.get(Calendar.MONTH) &&
//                            displayedMonth.get(Calendar.YEAR) == initialDate.get(Calendar.YEAR)
//
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .clip(CircleShape)
//                            .background(if (isSelected) AddIncomeBGColor else Color.Transparent)
//                            .clickable { onDateSelected(date) },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "${day + 1}",
//                            color = if (isSelected) Color.White else Color.Black
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Preview(showBackground = true, name = "Add New Transaction")
//@Composable
//fun AddTransactionPreview() {
//    val navController = rememberNavController()
//    Surface {
//        AddTransaction(navController = navController)
//    }
//}
//
//////////////// Version 2 ///////////////////
//@Composable
//fun AddTransaction(
//    navController: NavController,
//    transactionToEdit: TransactionData? = null,
//    modifier: Modifier = Modifier) {
//    // State untuk mode edit atau tambah baru
//    val isEditMode = transactionToEdit != null
//
//    // State untuk input pengguna
//    var selectedType by remember { mutableStateOf(transactionToEdit?.type?.replaceFirstChar { it.uppercase() } ?: "Expense") }
//    var amount by remember { mutableStateOf(transactionToEdit?.amount?.toString() ?: "") }
//    var selectedCategory by remember { mutableStateOf<Pair<String, Int>?>(
//        if(isEditMode) Pair(transactionToEdit!!.category, transactionToEdit.image) else null
//    ) }
//    // NOTE: PaymentMethod tidak ada di TransactionData. Ini perlu ditambahkan.
//    var selectedPaymentMethod by remember { mutableStateOf<Pair<String, Int>?>(null) }
//
//    // State untuk tanggal dan waktu
//    val initialCalendar = Calendar.getInstance().apply {
//        if(isEditMode) {
//            set(Calendar.YEAR, transactionToEdit!!.year)
//            // NOTE: Konversi nama bulan ke nomor bulan diperlukan.
//            // Ini adalah contoh sederhana.
//            val monthInt = SimpleDateFormat("MMMM", Locale.ENGLISH).parse(transactionToEdit.month)?.let {
//                Calendar.getInstance().apply { time = it }.get(Calendar.MONTH)
//            } ?: get(Calendar.MONTH)
//            set(Calendar.MONTH, monthInt)
//            set(Calendar.DAY_OF_MONTH, transactionToEdit.date)
//        }
//    }
//    var selectedDate by remember { mutableStateOf(initialCalendar) }
//    // NOTE: Waktu tidak ada di TransactionData. Ini perlu ditambahkan.
//    var selectedHour by remember { mutableStateOf(12) }
//    var selectedMinute by remember { mutableStateOf(0) }
//    var selectedIsAm by remember { mutableStateOf(false) } // false for PM, true for AM
//
//
//    // State untuk visibility overlay
//    var showCategoryOverlay by remember { mutableStateOf(false) }
//    var showPaymentMethodOverlay by remember { mutableStateOf(false) }
//    var showTimePickerOverlay by remember { mutableStateOf(false) }
//    var showCalendarOverlay by remember { mutableStateOf(false) }
//
//    val dateFormat = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.US) }
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(Color(0xFF178237), Color(0xFF1B1C28)),
//                    startY = 0f,
//                    endY = 800f
//                )
//            )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(26.dp)
//        ) {
//            // Top Bar: Tombol kembali dan Judul
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.arrow_side_line_left),
//                    contentDescription = "Back",
//                    modifier = Modifier
//                        .size(25.dp)
//                        .clickable { navController.popBackStack() }
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(
//                    text = if (isEditMode) "Edit Transaction" else "Add Transaction",
//                    color = Color.White,
//                    style = MaterialTheme.typography.headlineSmall,
//                )
//            }
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            // Toggle Income / Expense
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(40.dp)
//                    .clip(RoundedCornerShape(20.dp))
//            ) {
//                TransactionTypeButton(
//                    text = "Income",
//                    isSelected = selectedType == "Income",
//                    onClick = { selectedType = "Income" },
//                    modifier = Modifier.weight(1f)
//                )
//                TransactionTypeButton(
//                    text = "Expense",
//                    isSelected = selectedType == "Expense",
//                    onClick = { selectedType = "Expense" },
//                    modifier = Modifier.weight(1f)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            // Tanggal dan Waktu
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                DateDisplay(
//                    date = dateFormat.format(selectedDate.time),
//                    onClick = { showCalendarOverlay = true }
//                )
//                TimeDisplay(
//                    hour = selectedHour,
//                    minute = selectedMinute,
//                    isAm = selectedIsAm,
//                    onClick = { showTimePickerOverlay = true }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(40.dp))
//
//            // Form Fields
//            AmountField(value = amount, onValueChange = { amount = it })
//            Spacer(modifier = Modifier.height(20.dp))
//            TransactionField(
//                iconRes = selectedCategory?.second,
//                value = selectedCategory?.first ?: "Select Category",
//                isClickable = true,
//                onClick = { showCategoryOverlay = true }
//            )
//            Spacer(modifier = Modifier.height(20.dp))
//            TransactionField(
//                iconRes = selectedPaymentMethod?.second,
//                value = selectedPaymentMethod?.first ?: "Select Method",
//                isClickable = true,
//                onClick = { showPaymentMethodOverlay = true }
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // Tombol Delete dan Save
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 20.dp)
//            ) {
//                if (isEditMode) {
//                    ActionButton(
//                        text = "Delete",
//                        iconRes = R.drawable.delete_icon,
//                        backgroundColor = Color(0xFFFF6A6A),
//                        onClick = {
//                            // TODO: Tambahkan logika untuk menghapus transaksi dari list
//                            // TransactionLists.remove(transactionToEdit)
//                            navController.popBackStack()
//                        }
//                    )
//                    Spacer(modifier = Modifier.width(20.dp))
//                }
//                ActionButton(
//                    text = "Save",
//                    iconRes = R.drawable.save_icon,
//                    backgroundColor = Color(0xFF54E871),
//                    onClick = {
//                        val finalAmount = amount.toIntOrNull()
//                        if (finalAmount != null && selectedCategory != null && selectedPaymentMethod != null) {
//                            val newTransaction = TransactionData(
//                                type = selectedType.lowercase(),
//                                category = selectedCategory!!.first,
//                                date = selectedDate.get(Calendar.DAY_OF_MONTH),
//                                month = selectedDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) ?: "",
//                                year = selectedDate.get(Calendar.YEAR),
//                                image = selectedCategory!!.second,
//                                amount = finalAmount,
//                            )
//
//                            if (isEditMode) {
//                                // TODO: Logika untuk update transaksi yang ada
//                            } else {
//                                // Menambahkan transaksi baru ke awal list
//                                TransactionLists.add(0, newTransaction)
//                            }
//
//                            navController.popBackStack()
//                        }
//                    }
//                )
//            }
//        }
//
//        // Overlays
//        if (showCategoryOverlay) {
//            CategorySelectionOverlay(
//                transactionType = selectedType,
//                onDismiss = { showCategoryOverlay = false },
//                onCategorySelected = { categoryName, iconRes ->
//                    selectedCategory = Pair(categoryName, iconRes)
//                    showCategoryOverlay = false
//                }
//            )
//        }
//        if (showPaymentMethodOverlay) {
//            PaymentMethodOverlay(
//                onDismiss = { showPaymentMethodOverlay = false },
//                onPaymentMethodSelected = { methodName, iconRes ->
//                    selectedPaymentMethod = Pair(methodName, iconRes)
//                    showPaymentMethodOverlay = false
//                }
//            )
//        }
//        if (showTimePickerOverlay) {
//            TimePickerOverlay(
//                initialHour = selectedHour,
//                initialMinute = selectedMinute,
//                initialIsAm = selectedIsAm,
//                onDismiss = { showTimePickerOverlay = false },
//                onTimeSelected = { hour, minute, isAm ->
//                    selectedHour = hour
//                    selectedMinute = minute
//                    selectedIsAm = isAm
//                    showTimePickerOverlay = false
//                }
//            )
//        }
//        if (showCalendarOverlay) {
//            CalendarOverlay(
//                initialDate = selectedDate,
//                onDismiss = { showCalendarOverlay = false },
//                onDateSelected = { newDate ->
//                    selectedDate = newDate
//                    showCalendarOverlay = false
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun TransactionTypeButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
//    val backgroundColor = when {
//        isSelected && text == "Income" -> AddIncomeBGColor
//        isSelected && text == "Expense" -> AddExpenseBGColor
//        else -> UnselectedColor
//    }
//
//    Button(
//        onClick = onClick,
//        shape = RoundedCornerShape(20.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = backgroundColor,
//            contentColor = if (isSelected) Color.White else Color.Black
//        ),
//        modifier = modifier.fillMaxSize(),
//        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
//    ) {
//        Text(text = text, fontSize = 14.sp)
//    }
//}
//
//@Composable
//fun DateDisplay(date: String, onClick: () -> Unit) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.clickable(onClick = onClick)
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.calendar_icon),
//            contentDescription = "calendar_icon",
//            modifier = Modifier.size(30.dp),
//            colorFilter = ColorFilter.tint(Color.White)
//        )
//        Spacer(Modifier.width(8.dp))
//        Text(text = date, color = Color.White, style = TextStyle(fontSize = 14.sp))
//    }
//}
//
//@Composable
//fun TimeDisplay(hour: Int, minute: Int, isAm: Boolean, onClick: () -> Unit) {
//    val period = if (isAm) "AM" else "PM"
//    val formattedHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
//    val formattedMinute = String.format("%02d", minute)
//    val timeString = "$formattedHour:$formattedMinute $period"
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.clickable(onClick = onClick)
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.clock_icon),
//            contentDescription = "clock_icon",
//            colorFilter = ColorFilter.tint(Color.White),
//            modifier = Modifier.size(30.dp)
//        )
//        Spacer(Modifier.width(8.dp))
//        Text(text = timeString, color = Color.White, style = TextStyle(fontSize = 14.sp))
//    }
//}
//
//
//@Composable
//fun AmountField(value: String, onValueChange: (String) -> Unit) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = "Rp",
//                color = Color.White.copy(alpha = 0.7f),
//                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
//                modifier = Modifier.padding(end = 8.dp)
//            )
//            BasicTextField(
//                value = value,
//                onValueChange = onValueChange,
//                textStyle = TextStyle(
//                    color = Color.White,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.End
//                ),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                singleLine = true,
//                modifier = Modifier.weight(1f)
//            )
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Divider(color = Color.White.copy(alpha = 0.5f))
//    }
//}
//
//@Composable
//fun TransactionField(iconRes: Int?, value: String, isClickable: Boolean = false, onClick: () -> Unit = {}) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .then(if (isClickable) Modifier.clickable(onClick = onClick) else Modifier)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            if (iconRes != null) {
//                Image(
//                    painter = painterResource(id = iconRes),
//                    contentDescription = value,
//                    modifier = Modifier.size(30.dp),
//                    colorFilter = ColorFilter.tint(Color.White)
//                )
//                Spacer(modifier = Modifier.width(12.dp))
//            }
//            Text(
//                text = value,
//                color = Color.White,
//                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Light),
//                modifier = Modifier.weight(1f)
//            )
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Divider(color = Color.White.copy(alpha = 0.5f))
//    }
//}
//
//@Composable
//fun ActionButton(text: String, iconRes: Int, backgroundColor: Color, onClick: () -> Unit) {
//    Button(
//        onClick = onClick,
//        shape = RoundedCornerShape(16.dp),
//        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
//        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
//        modifier = Modifier.height(50.dp)
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                painter = painterResource(id = iconRes),
//                contentDescription = text,
//                tint = Color.White,
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
//        }
//    }
//}
//
//@Composable
//fun CategorySelectionOverlay(
//    transactionType: String,
//    onDismiss: () -> Unit,
//    onCategorySelected: (name: String, iconRes: Int) -> Unit
//) {
//    val categories = if (transactionType == "Income") categoryIncome else categoryExpense
//    val gradient = if (transactionType == "Income") {
//        Brush.linearGradient(listOf(Color(0xFF2f9944), Color(0xFF54E871)))
//    } else {
//        Brush.linearGradient(listOf(Color(0xFFFF6A6A), Color(0xFF994040)))
//    }
//
//    Dialog(onDismissRequest = onDismiss) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(0.95f)
//                .clip(RoundedCornerShape(24.dp))
//                .background(brush = gradient)
//                .padding(16.dp)
//        ) {
//            Column {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Category $transactionType",
//                        color = Color.White,
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Icon(
//                        painter = painterResource(id = R.drawable.icone_close),
//                        contentDescription = "Close",
//                        tint = Color.White,
//                        modifier = Modifier
//                            .size(24.dp)
//                            .clickable(onClick = onDismiss)
//                    )
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(3),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(categories.entries.toList()) { (name, iconRes) ->
//                        CategoryItem(
//                            name = name,
//                            iconRes = iconRes,
//                            onClick = { onCategorySelected(name, iconRes) }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CategoryItem(name: String, iconRes: Int, onClick: () -> Unit) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .clickable(onClick = onClick)
//            .padding(8.dp)
//    ) {
//        Image(
//            painter = painterResource(id = iconRes),
//            contentDescription = name,
//            modifier = Modifier.size(40.dp),
//            colorFilter = ColorFilter.tint(Color.White)
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = name,
//            color = Color.White,
//            textAlign = TextAlign.Center,
//            fontSize = 12.sp
//        )
//    }
//}
//
//@Composable
//fun PaymentMethodOverlay(onDismiss: () -> Unit, onPaymentMethodSelected: (name: String, iconRes: Int) -> Unit) {
//    val paymentMethods = listOf(
//        "Cash" to R.drawable.cash_icon,
//        "Debit Card" to R.drawable.credit_card,
//        "E-Wallet" to R.drawable.ewallet_icon,
//        "Others" to R.drawable.others
//    )
//    val gradient = Brush.linearGradient(listOf(Color(0xFF2f9944), Color(0xFF54E871)))
//
//
//    Dialog(onDismissRequest = onDismiss) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(0.95f)
//                .clip(RoundedCornerShape(24.dp))
//                .background(brush = gradient)
//                .padding(16.dp)
//        ) {
//            Column {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text("Payment Method", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
//                    Icon(
//                        painter = painterResource(id = R.drawable.icone_close),
//                        contentDescription = "Close",
//                        tint = Color.White,
//                        modifier = Modifier
//                            .size(24.dp)
//                            .clickable(onClick = onDismiss)
//                    )
//                }
//                Spacer(modifier = Modifier.height(24.dp))
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(3),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(paymentMethods) { (name, iconRes) ->
//                        CategoryItem(
//                            name = name,
//                            iconRes = iconRes,
//                            onClick = { onPaymentMethodSelected(name, iconRes) }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun TimePickerOverlay(
//    initialHour: Int, initialMinute: Int, initialIsAm: Boolean,
//    onDismiss: () -> Unit, onTimeSelected: (hour: Int, minute: Int, isAm: Boolean) -> Unit
//) {
//    var hour by remember { mutableStateOf(initialHour) }
//    var minute by remember { mutableStateOf(initialMinute) }
//    var isAm by remember { mutableStateOf(initialIsAm) }
//
//    Dialog(onDismissRequest = onDismiss) {
//        Column(
//            modifier = Modifier
//                .clip(RoundedCornerShape(24.dp))
//                .background(Color.White)
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                NumberPicker(value = hour, onValueChange = { hour = it }, range = 1..12)
//                Text(":", fontSize = 48.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
//                NumberPicker(value = minute, onValueChange = { minute = it }, range = 0..59)
//            }
//            Spacer(modifier = Modifier.height(24.dp))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(40.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(Color.LightGray.copy(alpha = 0.5f))
//            ) {
//                val amBgColor = if (isAm) Color(0xFF54E871) else Color.Transparent
//                val pmBgColor = if (!isAm) Color(0xFF54E871) else Color.Transparent
//
//                TextButton(
//                    onClick = {
//                        isAm = true
//                        onTimeSelected(hour, minute, true)
//                    },
//                    modifier = Modifier.weight(1f).fillMaxSize().background(amBgColor, RoundedCornerShape(12.dp)),
//                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
//                ) { Text("AM") }
//                TextButton(
//                    onClick = {
//                        isAm = false
//                        onTimeSelected(hour, minute, false)
//                    },
//                    modifier = Modifier.weight(1f).fillMaxSize().background(pmBgColor, RoundedCornerShape(12.dp)),
//                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
//                ) { Text("PM") }
//            }
//        }
//    }
//}
//
//@Composable
//fun NumberPicker(value: Int, onValueChange: (Int) -> Unit, range: IntRange) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Icon(
//            painter = painterResource(id = R.drawable.arrow_up),
//            contentDescription = "Increase",
//            modifier = Modifier.size(36.dp).clickable {
//                onValueChange((value + 1).let { if (it > range.last) range.first else it })
//            }
//        )
//        Text(String.format("%02d", value), fontSize = 48.sp, fontWeight = FontWeight.Bold)
//        Icon(
//            painter = painterResource(id = R.drawable.arrow_down),
//            contentDescription = "Decrease",
//            modifier = Modifier.size(36.dp).clickable {
//                onValueChange((value - 1).let { if (it < range.first) range.last else it })
//            }
//        )
//    }
//}
//
//
//@Composable
//fun CalendarOverlay(initialDate: Calendar, onDismiss: () -> Unit, onDateSelected: (Calendar) -> Unit) {
//    var displayedMonth by remember { mutableStateOf(initialDate.clone() as Calendar) }
//
//    Dialog(onDismissRequest = onDismiss) {
//        Column(
//            modifier = Modifier
//                .clip(RoundedCornerShape(24.dp))
//                .background(Color.White)
//                .padding(16.dp)
//        ) {
//            // Header: Month and Year Navigation
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.arrow_side_line_left),
//                    contentDescription = "Previous Month",
//                    modifier = Modifier.size(24.dp).clickable {
//                        displayedMonth = (displayedMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
//                    }
//                )
//                Text(
//                    text = SimpleDateFormat("MMMM yyyy", Locale.US).format(displayedMonth.time),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//                Icon(
//                    painter = painterResource(id = R.drawable.arrow_side_line_right),
//                    contentDescription = "Next Month",
//                    modifier = Modifier.size(24.dp).clickable {
//                        displayedMonth = (displayedMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
//                    }
//                )
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//            Divider()
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Day of Week Headers
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
//                val days = listOf("SEN", "SEL", "RAB", "KAM", "JUM", "SAB", "MIN")
//                days.forEach { Text(it, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray) }
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Calendar Dates
//            val daysInMonth = displayedMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
//            val firstDayOfMonth = (displayedMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK)
//            val offset = (firstDayOfMonth - Calendar.MONDAY + 7) % 7
//
//            LazyVerticalGrid(columns = GridCells.Fixed(7)) {
//                items(offset) { Spacer(modifier = Modifier.size(40.dp)) } // Empty spacers for offset
//                items(daysInMonth) { day ->
//                    val date = (displayedMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, day + 1) }
//                    val isSelected = day + 1 == initialDate.get(Calendar.DAY_OF_MONTH) &&
//                            displayedMonth.get(Calendar.MONTH) == initialDate.get(Calendar.MONTH) &&
//                            displayedMonth.get(Calendar.YEAR) == initialDate.get(Calendar.YEAR)
//
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .clip(CircleShape)
//                            .background(if (isSelected) AddIncomeBGColor else Color.Transparent)
//                            .clickable { onDateSelected(date) },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "${day + 1}",
//                            color = if (isSelected) Color.White else Color.Black
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun AddTransactionPreview() {
//    val navController = rememberNavController()
//    AddTransaction(navController, modifier = Modifier)
//}
