package com.example.spendoov2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendoov2.ui.theme.BottomNavColor
import com.example.spendoov2.ui.theme.CardInfoBackgroundColor
import com.example.spendoov2.ui.theme.MainBackgroundColor
import com.example.spendoov2.ui.theme.SpendooV2Theme
import com.example.spendoov2.ui.theme.interFamily
import com.example.spendoov2.ui.theme.interTextStyle
import com.example.spendoov2.ui.theme.poppinsTextStyle
import com.example.spendoov2.ui.theme.unboundedFamily
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spendoov2.ui.theme.*
import java.time.LocalDate
import java.util.Calendar




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        generateTransactionData(50)
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
                    navController = navController,
                    onSettingsClick = { showSettings = true } // Tampilkan settings overlay
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

        // Overlays
        if (showSettings) {
            SettingsOverlay(
                onDismiss = { showSettings = false },
                onExportClick = {
                    showSettings = false
                    showExport = true
                },
                onEditProfileClick = { /* TODO */ },
                onLogoutClick = {
                    showSettings = false
                    showLogout = true
                }
            )
        }

        if (showExport) {
            ExportOptionsOverlay(
                onDismiss = { showExport = false },
                onAllTransactionsClick = { /* TODO */ },
                onDailyClick = { /* TODO */ },
                onMonthlyClick = { /* TODO */ }
            )
        }

        if (showLogout) {
            ConfirmationOverlay(
                message = "Are you sure you want to logout?",
                onConfirm = {
                    showLogout = false
                    onLogout()
                },
                onCancel = { showLogout = false }
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
    onSettingsClick: () -> Unit,
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
                            painter = painterResource(R.drawable.dot_option),
                            contentDescription = "Options",
                            modifier = modifier
                                .size(35.dp)
                                .clickable(onClick = onSettingsClick) // Perbaikan: Hanya satu clickable
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
                    text = "Hi,\nGuest",
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
                    text = "Rp${AvailableBalance()}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
    }
}

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

//@Composable
//fun Pages(
//    navController: NavController,
//    onLogout: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var pageType by remember { mutableStateOf("home") }
//    var contentType by remember { mutableStateOf("all") }
//
//    var showSettings by remember {mutableStateOf(false)}
//    var showExport by remember {mutableStateOf(false)}
//    var showLogout by remember {mutableStateOf(false)}
//
//    Column(
//        modifier = modifier
//            .fillMaxHeight()
//            .fillMaxWidth()
//            .background(MainBackgroundColor)
//    ) {
//        Column(
//            modifier = modifier
//                .weight(1f)
//        ) {
//            TopBanner(
//                contentType,
//                pageType,
//                onClick = { newTab ->
//                    contentType = newTab
//                },
//                onNavigateToPage = { newPage ->
//                    pageType = newPage
//                },
//                navController = navController,
//                onSettingsClick = {showSettings = true})
//            PageContent(
//                contentType,
//                pageType
//            )
//
//        }
//        BottomNav(
//            onClick = { action ->
//                contentType = action
//            },
//            navController = navController
//        )
//    }
//    // 1. Tampilkan Settings Overlay
//    if (showSettings) {
//        SettingsOverlay(
//            onDismiss = { showSettings = false },
//            onExportClick = {
//                showSettings = false // Sembunyikan settings
//                showExport = true    // Tampilkan export options
//            },
//            onEditProfileClick = { /* TODO: Handle edit profile click */ },
//            onLogoutClick = {
//                showSettings = false // Sembunyikan settings
//                showLogout = true    // Tampilkan konfirmasi logout
//            }
//        )
//    }
//
//    // 2. Tampilkan Export Options Overlay
//    if (showExport) {
//        ExportOptionsOverlay(
//            onDismiss = { showExport = false },
//            onAllTransactionsClick = { /* TODO: Handle export all */ },
//            onDailyClick = { /* TODO: Handle export daily */ },
//            onMonthlyClick = { /* TODO: Handle export monthly */ }
//        )
//    }
//
//    // 3. Tampilkan Logout Confirmation Overlay
//    if (showLogout) {
//        ConfirmationOverlay(
//            message = "Are you sure you want to logout?",
//            onConfirm = {
//                showLogout = false // Sembunyikan konfirmasi
//                onLogout() // Panggil fungsi logout dari AppNavigation
//            },
//            onCancel = { showLogout = false } // Sembunyikan saat batal
//        )
//    }
//}
//
//@Composable
//fun TopBanner(
//    contentTabs: String,
//    content: String,
//    onClick: (String) -> Unit,
//    onNavigateToPage: (String) -> Unit,
//    navController: NavController,
//    textStyle: TextStyle = interTextStyle,
//    modifier: Modifier = Modifier,
//    onSettingsClick: () -> Unit
//) {
//
//    CompositionLocalProvider(LocalTextStyle provides textStyle) {
//        Column(
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(24.dp, 35.dp)
//
//        ) {
//            when (content) {
//                "home" -> {
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = modifier
//                            .fillMaxWidth()
//                    ) {
//                        LogoContainer(textStyle)
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            modifier = modifier
//                                .width(75.dp)
//                                .size(50.dp)
//                        ) {
//                            Image(
//                                painter = painterResource(R.drawable.search_icon),
//                                contentDescription = "Search",
//                                modifier = modifier
//                                    .clickable { navController.navigate("search_screen") }
//                            )
//                            Image(
//                                painter = painterResource(R.drawable.dot_option),
//                                contentDescription = "Options",
//                                modifier = modifier
//                                    .size(35.dp)
//                                    .clickable {onSettingsClick()}                                    .clickable {navController.navigate("")}
//                            )
//                        }
//                    }
//                    Spacer(modifier = modifier.padding(5.dp))
//                    NavBarHome(
//                        currentTab = contentTabs,
//                        onClick = onClick
//                    )
//                }
//
//                "search" -> {
//
//                }
//
//                "edit" -> {
//
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun LogoContainer(
//    textStyle: TextStyle,
//    modifier: Modifier = Modifier
//) {
//    CompositionLocalProvider(LocalTextStyle provides textStyle) {
//        Column(
//            modifier = modifier
//                .width(150.dp)
//        ) {
//            Text(
//                text = "SPENDOO",
//                fontSize = 20.sp,
//                fontWeight = FontWeight(700),
//                fontFamily = unboundedFamily,
//                modifier = modifier
//                    .fillMaxWidth()
//            )
//            Text(
//                text = "You Earn, We Learn",
//                fontFamily = interFamily,
//                modifier = modifier
//                    .fillMaxWidth()
//            )
//        }
//    }
//}
//
//@Composable
//fun PageContent(
//    content: String,
//    pageContent: String,
//    textStyle: TextStyle = interTextStyle,
//    modifier: Modifier = Modifier
//) {
//    when (pageContent) {
//        "home" -> {
//            when (content) {
//                "all" -> {
//                    QuickInfoCard()
//                    CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
//
//                        Column(
//                            modifier = modifier
//                                .padding(28.dp, 12.dp)
//                                .fillMaxWidth()
//                        ) {
//                            Text(
//                                text = "Recent Transactions",
//                                fontSize = 18.sp,
//                                color = Color.White
//                            )
//                            Text(
//                                text = "Last 7 Days",
//                                fontSize = 12.sp,
//                                color = Color.White
//                            )
//                        }
//                    }
//                    RecentTransactionList(filterType = null)
//                }
//
//                "daily" -> {
//
//                    var transactionType: String? by remember { mutableStateOf(null) }
//
//                    DateNavBar()
//                    IncomeExpenseNavBar(
//                        activeTab = transactionType,
//                        onTabSelected = { newType ->
//                            transactionType = newType
//                        }
//                    )
//                    RecentTransactionList(filterType = transactionType)
//                }
//
//                "monthly" -> {
//                    DateNavBar()
//                    MonthlyList()
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun BottomNav(
//    onClick: (String) -> Unit,
//    navController: NavController,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        horizontalArrangement = Arrangement.SpaceAround,
//        modifier = modifier
//            .fillMaxWidth()
//            .height(70.dp)
//            .background(BottomNavColor)
//            .padding(12.dp)
//    ) {
//        Image(
//            painter = painterResource(R.drawable.home_icon),
//            contentDescription = null,
//            modifier = modifier
//                .clickable { onClick("all") }
//        )
//        Image(
//            painter = painterResource(R.drawable.plus_icon),
//            contentDescription = null,
//            modifier = modifier
//                .clickable{navController.navigate("add_screen")}
//        )
//        Image(
//            painter = painterResource(R.drawable.insight_icon),
//            contentDescription = null,
//            modifier = modifier
//        )
//    }
//}
//
//@Composable
//fun QuickInfoCard(modifier: Modifier = Modifier) {
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
//                    text = "Hi,\nGuest",
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
//                    text = "Rp${AvailableBalance()}",
//                    fontSize = 26.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    modifier = modifier.fillMaxWidth()
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun Spendoo(onLogout: () -> Unit = {}) {
//    TransactionData(50)
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "home_screen") {
//
//        composable("home_screen") {
//
//            Pages(navController = navController, onLogout = onLogout)
//        }
//        composable("search_screen") {
//            SearchPage(navController = navController)
//        }
//        composable("add_screen") {
//            AddTransaction(navController = navController)
//        }
//
//    }
//}

@Preview
@Composable
fun HomePagePreview() {
    SpendooV2Theme {
        Spendoo()
    }
}
