package com.example.spendoov2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spendoov2.ui.theme.BottomNavColor
import com.example.spendoov2.ui.theme.CardInfoBackgroundColor
import com.example.spendoov2.ui.theme.MainBackgroundColor
import com.example.spendoov2.ui.theme.SpendooV2Theme
import com.example.spendoov2.ui.theme.interFamily
import com.example.spendoov2.ui.theme.interTextStyle
import com.example.spendoov2.ui.theme.poppinsTextStyle
import com.example.spendoov2.ui.theme.unboundedFamily
import java.time.LocalDate
import java.util.Calendar
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import com.google.firebase.auth.UserProfileChangeRequest
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        generateTransactionData(100)
        enableEdgeToEdge()
        setContent {
            SpendooV2Theme {
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    AppNavigation()
                }

            }
        }
    }
}


@Composable
fun Pages(
    navController: NavController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var pageType by remember { mutableStateOf("home") }
    var contentType by remember { mutableStateOf("all") }

    // State untuk visibility overlay
    var showSettings by remember { mutableStateOf(false) }
    var showExport by remember { mutableStateOf(false) }
    var showLogout by remember { mutableStateOf(false) }

    // State untuk filter tanggal
    var dailyDate by remember { mutableStateOf(LocalDate.now()) }
    var monthlyDate by remember { mutableStateOf(LocalDate.now()) }
    var showCalendar by remember { mutableStateOf(false) }
    var showMonthPicker by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainBackgroundColor)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TopBanner(
                    contentTabs = contentType,
                    content = pageType,
                    onClick = { newTab -> contentType = newTab },
                    onNavigateToPage = { newPage -> pageType = newPage },
                    navController = navController
                )
                PageContent(
                    content = contentType,
                    pageContent = pageType,
                    navController = navController,
                    dailyDate = dailyDate,
                    monthlyDate = monthlyDate,
                    onDailyDateChange = { dailyDate = it },
                    onShowCalendar = { showCalendar = true },
                    onMonthlyDateChange = { monthlyDate = it },
                    onShowMonthPicker = { showMonthPicker = true }
                )
            }
            BottomNav(
                onClick = { action -> contentType = action },
                navController = navController
            )
        }

        if (showCalendar) {
            val calendar = Calendar.getInstance().apply {
                set(dailyDate.year, dailyDate.monthValue - 1, dailyDate.dayOfMonth)
            }
            CalendarOverlay(
                initialDate = calendar,
                onDismiss = { showCalendar = false },
                onDateSelected = { newDate ->
                    dailyDate = LocalDate.of(
                        newDate.get(Calendar.YEAR),
                        newDate.get(Calendar.MONTH) + 1,
                        newDate.get(Calendar.DAY_OF_MONTH)
                    )
                    showCalendar = false
                }
            )
        }

        if (showMonthPicker) {
            MonthYearPickerOverlay(
                initialDate = monthlyDate,
                onDismiss = { showMonthPicker = false },
                onDateSelected = { newDate ->
                    monthlyDate = newDate
                    showMonthPicker = false
                }
            )
        }
    }
}

@Composable
fun TopBanner(
    contentTabs: String,
    content: String,
    onClick: (String) -> Unit,
    onNavigateToPage: (String) -> Unit,
    navController: NavController,
    textStyle: TextStyle = interTextStyle,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalTextStyle provides textStyle) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp, 35.dp)
        ) {
            if (content == "home") {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.fillMaxWidth()
                ) {
                    LogoContainer(textStyle)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = "Search",
                            modifier = modifier
                                .size(30.dp)
                                .clickable { navController.navigate("search_screen") }
                        )
                        Image(
                            painter = painterResource(R.drawable.profile_icon),
                            contentDescription = "Profile",
                            modifier = modifier
                                .size(35.dp)
                                .clickable{navController.navigate("profile_screen")} // Perbaikan: Hanya satu clickable
                        )
                    }
                }
                Spacer(modifier = modifier.padding(5.dp))
                NavBarHome(currentTab = contentTabs, onClick = onClick)
            }
        }
    }
}

@Composable
fun LogoContainer(
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalTextStyle provides textStyle) {
        Column(
            modifier = modifier
                .width(150.dp)
        ) {
            Text(
                text = "SPENDOO",
                fontSize = 20.sp,
                fontWeight = FontWeight(700),
                fontFamily = unboundedFamily,
                modifier = modifier
                    .fillMaxWidth()
            )
            Text(
                text = "You Earn, We Learn",
                fontFamily = interFamily,
                modifier = modifier
                    .fillMaxWidth()
            )
        }
    }
}


@Composable
fun PageContent(
    content: String,
    pageContent: String,
    navController: NavController,
    dailyDate: LocalDate,
    monthlyDate: LocalDate,
    onDailyDateChange: (LocalDate) -> Unit,
    onShowCalendar: () -> Unit,
    onMonthlyDateChange: (LocalDate) -> Unit,
    onShowMonthPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onTransactionClick: (String) -> Unit = { transactionId ->
        navController.navigate("add_screen/$transactionId")
    }

    if (pageContent == "home") {
        when (content) {
            "all" -> {
                QuickInfoCard()
                RecentTransactionsHeader()
                RecentTransactionList(
                    filterType = null,
                    selectedDate = null, // Tampilkan 7 hari terakhir
                    onTransactionClick = onTransactionClick
                )
            }
            "daily" -> {
                var transactionType: String? by remember { mutableStateOf(null) }
                DateNavBar(
                    currentDate = dailyDate,
                    onDateClick = onShowCalendar,
                    onPrevious = { onDailyDateChange(dailyDate.minusDays(1)) },
                    onNext = { onDailyDateChange(dailyDate.plusDays(1)) }
                )
                IncomeExpenseNavBar(
                    activeTab = transactionType,
                    onTabSelected = { newType -> transactionType = newType }
                )
                RecentTransactionList(
                    filterType = transactionType,
                    selectedDate = dailyDate,
                    onTransactionClick = onTransactionClick
                )
            }
            "monthly" -> {
                DateNavBar(
                    currentDate = monthlyDate,
                    onDateClick = onShowMonthPicker,
                    onPrevious = { onMonthlyDateChange(monthlyDate.minusMonths(1)) },
                    onNext = { onMonthlyDateChange(monthlyDate.plusMonths(1)) },
                    isMonthSelector = true
                )
                MonthlyList(
                    selectedDate = monthlyDate,
                    onTransactionClick = onTransactionClick
                )
            }
        }
    }
}

@Composable
fun QuickInfoCard(modifier: Modifier = Modifier) {
    // State untuk menampung data
    var transactions by remember { mutableStateOf(emptyList<TransactionData>()) }
    var userName by remember { mutableStateOf("Guest") }

    // Dapatkan instance Auth dan Firestore
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    // DisposableEffect akan mengelola listener kita.
    // Ini akan berjalan saat composable dimuat dan akan 'membersihkan' (onDispose)
    // listener saat composable hancur/ditinggalkan.
    DisposableEffect(auth) {
        // 1. Buat Listener Status Autentikasi
        //    Listener ini akan bereaksi terhadap Login, Logout, DAN pembaruan profil
        //    (karena pembaruan profil biasanya memicu pembaruan token).
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            // Perbarui state 'userName' yang akan memicu recomposition
            userName = currentUser?.displayName?.takeIf { it.isNotBlank() } ?: "Guest"
        }
        auth.addAuthStateListener(authListener)

        // 2. Buat Listener Transaksi
        val currentUser = auth.currentUser
        var transactionListener: com.google.firebase.firestore.ListenerRegistration? = null

        if (currentUser != null) {
            // --- MODE LOGIN ---
            // Set nama awal saat pertama kali dimuat
            userName = currentUser.displayName?.takeIf { it.isNotBlank() } ?: "Guest"

            // Pasang listener snapshot untuk transaksi
            transactionListener = db.collection("users")
                .document(currentUser.uid)
                .collection("transactions")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("Firestore", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val firestoreTransactions = snapshot.documents.map { doc ->
                            doc.toObject(TransactionData::class.java)?.copy(id = doc.id)
                        }
                        transactions = firestoreTransactions.filterNotNull()
                    }
                }
        } else {
            // --- MODE GUEST ---
            userName = "Guest"
            transactions = TransactionLists.toList()
        }

        // 3. Fungsi Cleanup (PENTING)
        //    Ini akan dijalankan ketika composable hancur (misal: pindah layar)
        //    untuk mencegah kebocoran memori.
        onDispose {
            auth.removeAuthStateListener(authListener)
            transactionListener?.remove() // Hentikan pendengar snapshot
        }
    }

    // --- UI (Sama seperti kode Anda sebelumnya) ---
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(28.dp, 0.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(CardInfoBackgroundColor)
            .height(170.dp)
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
            Column(
                modifier = modifier.padding(18.dp, 12.dp)
            ) {

                Text(
                    text = "Hi,\n$userName", // <-- Sekarang menggunakan state 'userName'
                    fontSize = 32.sp,
                    textAlign = TextAlign.Left,
                    modifier = modifier.fillMaxWidth()
                )
                Text(
                    text = "Available Balance",
                    fontSize = 12.sp,
                    modifier = modifier.fillMaxWidth()
                )
                Text(
                    text = "Rp${AvailableBalance(transactions)}", // <-- Menggunakan state 'transactions'
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
    }
}

//@Composable
//fun QuickInfoCard(modifier: Modifier = Modifier) {
//    // State baru untuk menampung data
//    var transactions by remember { mutableStateOf(emptyList<TransactionData>()) }
//
//    // Dapatkan instance Auth dan Firestore
//    val auth = FirebaseAuth.getInstance()
//    val db = Firebase.firestore
//    val currentUser = auth.currentUser
//
//    // Effect ini akan berjalan saat composable dimuat & jika currentUser berubah
//    LaunchedEffect(currentUser) {
//        if (currentUser != null) {
//            // --- MODE LOGIN: Ambil data dari Firestore ---
//            db.collection("users")
//                .document(currentUser.uid)
//                .collection("transactions")
//                .addSnapshotListener { snapshot, e ->
//                    if (e != null) {
//                        Log.w("Firestore", "Listen failed.", e)
//                        return@addSnapshotListener
//                    }
//                    if (snapshot != null) {
//                        val firestoreTransactions = snapshot.documents.map { doc ->
//                            doc.toObject(TransactionData::class.java)?.copy(id = doc.id)
//                        }
//                        transactions = firestoreTransactions.filterNotNull()
//                    }
//                }
//        } else {
//            // --- MODE GUEST: Ambil data dari List lokal ---
//            transactions = TransactionLists.toList()
//        }
//    }
//
//    val userName = if (currentUser != null && !currentUser.displayName.isNullOrBlank()) {
//        currentUser.displayName // Ambil nama dari profil Auth
//    } else {
//        "Guest" // Fallback
//    }
//
//    Column(
//        verticalArrangement = Arrangement.SpaceBetween,
//        modifier = modifier
//            .padding(28.dp, 0.dp)
//            .clip(RoundedCornerShape(32.dp))
//            .background(CardInfoBackgroundColor)
//            .height(170.dp)
//            .fillMaxWidth()
//    ) {
//        CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
//            Column(
//                modifier = modifier.padding(18.dp, 12.dp)
//            ) {
//
//                Text(
//                    text = "Hi,\n$userName",
//                    fontSize = 32.sp,
//                    textAlign = TextAlign.Left,
//                    modifier = modifier.fillMaxWidth()
//                )
//                Text(
//                    text = "Available Balance",
//                    fontSize = 12.sp,
//                    modifier = modifier.fillMaxWidth()
//                )
//                Text(
//                    text = "Rp${AvailableBalance(transactions)}",
//                    fontSize = 26.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    modifier = modifier.fillMaxWidth()
//                )
//            }
//        }
//    }
//}

@Composable
fun BottomNav(
    onClick: (String) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BottomNavColor, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "Home" Icon
        Image(
            painter = painterResource(id = R.drawable.home_icon), // Make sure you have 'home_icon.xml' in your res/drawable
            contentDescription = "Home",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    // Navigate to home logic, e.g., reset content type
                    onClick("all")
                }
        )

        // "Add Transaction" Button
        Image(
            painter = painterResource(id = R.drawable.plus_icon), // Make sure you have 'add_button.xml'
            contentDescription = "Add Transaction",
            modifier = Modifier
                .size(60.dp) // Typically larger
                .clickable {
                    navController.navigate("add_screen/new") // Navigate to add/edit screen
                }
        )

        // "Profile" or "Statistics" Icon
        Image(
            painter = painterResource(id = R.drawable.insight_icon), // Make sure you have 'statistic_icon.xml'
            contentDescription = "Statistics",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    navController.navigate("statistics_screen") // Example navigation
                }
        )
    }
}


@Composable
fun Spendoo(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") {
            Pages(navController = navController, onLogout = onLogout)
        }
        composable("search_screen") {
            SearchPage(navController = navController)
        }
        // Rute untuk menambah atau mengedit transaksi
        composable(
            route = "add_screen/{transactionId}",
            arguments = listOf(navArgument("transactionId") { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            AddTransaction(navController = navController, transactionId = transactionId)
        }
        // Rute untuk menambah transaksi baru (tanpa ID)
        composable("add_screen") {
            AddTransaction(navController = navController)
        }
        composable("statistics_screen") {
            AnalyzeAndAdviceScreen(navController = navController)
        }

        composable("profile_screen") {
            ProfileScreen(navController = navController, onLogout = onLogout)
        }
        composable("reset_password_screen") {
            ResetPasswordScreen(navController = navController)
        }

        composable("forgot_password_screen") {
            ForgotPasswordPage(onNavigateBack = { navController.popBackStack() })
        }
    }
}


// Komponen lain seperti LogoContainer, RecentTransactionsHeader, BottomNav, QuickInfoCard, dll
// tetap sama seperti di file asli Anda.
// (Untuk keringkasan, kode-kode tersebut tidak disalin ulang di sini)
@Composable
fun RecentTransactionsHeader(modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
        Column(
            modifier = modifier
                .padding(28.dp, 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Recent Transactions",
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = "Last 7 Days",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            text = "Change Username"
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
            onAllTransactionsClick = { /* TODO: Logic Export Yearly */ showExport = false },
            onDailyClick = { /* TODO: Logic Export Daily */ showExport = false },
            onMonthlyClick = { /* TODO: Logic Export Monthly */ showExport = false },
            allText = "Yearly"
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
                }
                showChangeUsername = false
            }
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

@Composable
fun ProfileButton(
    text: String,
    icon: ImageVector? = null, // <-- DIPERBAIKI: Dibuat nullable
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
        // DIPERBAIKI: Ikon hanya ditampilkan jika 'icon' tidak null
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

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
fun ProfileButton(
    text: String,
    icon: ImageVector? = null,
    painter: Painter, // <-- Parameter untuk Painter
    contentDescription: String?, // <-- Parameter deskripsi untuk Painter
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
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )

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


@Preview
@Composable
fun HomePagePreview() {
    SpendooV2Theme {
        Spendoo()
    }
}
