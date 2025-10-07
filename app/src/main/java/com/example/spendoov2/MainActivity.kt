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
import com.example.spendoov2.ui.theme.BottomNavColor
import com.example.spendoov2.ui.theme.CardInfoBackgroundColor
import com.example.spendoov2.ui.theme.MainBackgroundColor
import com.example.spendoov2.ui.theme.SpendooV2Theme
import com.example.spendoov2.ui.theme.interFamily
import com.example.spendoov2.ui.theme.interTextStyle
import com.example.spendoov2.ui.theme.poppinsTextStyle
import com.example.spendoov2.ui.theme.unboundedFamily


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
fun AppNavigation(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf("splash") }

    when (currentScreen) {
        "splash" -> {
            SplashScreen(
                onNavigateToLogin = { currentScreen = "login" }
            )
        }
        "login" -> {
            LoginPage(
                onNavigateToHome = { currentScreen = "home" },
                onNavigateToSignUp = { currentScreen = "signup" }
            )
        }
        "signup" -> {
            SignUpPage(
                onNavigateBack = { currentScreen = "login" }
            )
        }
        "home" -> {
            Spendoo()
        }
    }
}

@Composable
fun Pages(modifier: Modifier = Modifier) {
    var pageType by remember { mutableStateOf("home") }
    var contentType by remember { mutableStateOf("all") }
    var showSettingsOverlay by remember { mutableStateOf(false) }
    var showExportOverlay by remember { mutableStateOf(false) }
    var showLogoutConfirmation by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(MainBackgroundColor)
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
            ) {
                TopBanner(
                    contentType,
                    pageType,
                    onClick = { newTab ->
                        contentType = newTab
                    },
                    onNavigateToPage = { newPage ->
                        pageType = newPage
                    },
                    onSettingsClick = {
                        showSettingsOverlay = true
                    }
                )
                PageContent(
                    contentType,
                    pageType
                )
            }
            BottomNav(
                onClick = { action ->
                    contentType = action
                }
            )
        }

        // Overlays
        if (showSettingsOverlay) {
            SettingsOverlay(
                onDismiss = {
                    showSettingsOverlay = false
                    showExportOverlay = false
                },
                onExportClick = {
                    showExportOverlay = true
                },
                onEditProfileClick = {
                    // Handle edit profile
                },
                onLogoutClick = {
                    showLogoutConfirmation = true
                }
            )
        }

        if (showExportOverlay) {
            ExportOptionsOverlay(
                onDismiss = {
                    showExportOverlay = false
                },
                onAllTransactionsClick = {
                    // Handle export all transactions
                    showExportOverlay = false
                    showSettingsOverlay = false
                },
                onDailyClick = {
                    // Handle export daily
                    showExportOverlay = false
                    showSettingsOverlay = false
                },
                onMonthlyClick = {
                    // Handle export monthly
                    showExportOverlay = false
                    showSettingsOverlay = false
                }
            )
        }

        if (showLogoutConfirmation) {
            ConfirmationOverlay(
                message = "Are you sure?",
                onConfirm = {
                    // Handle logout
                    showLogoutConfirmation = false
                    showSettingsOverlay = false
                },
                onCancel = {
                    showLogoutConfirmation = false
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
    onSettingsClick: () -> Unit = {},
    textStyle: TextStyle = interTextStyle,
    modifier: Modifier = Modifier
) {

    CompositionLocalProvider(LocalTextStyle provides textStyle) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp, 32.dp)

        ) {
            when (content) {
                "home" -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        LogoContainer(textStyle)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier
                                .width(50.dp)
                                .size(38.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.search_icon),
                                contentDescription = "Search",
                                modifier = modifier
                                    .clickable { onNavigateToPage("search") }
                            )
                            Image(
                                painter = painterResource(R.drawable.dot_option),
                                contentDescription = "Settings",
                                modifier = modifier
                                    .clickable { onSettingsClick() }
                            )
                        }
                    }
                    Spacer(modifier = modifier.padding(12.dp))
                    NavBarHome(
                        currentTab = contentTabs,
                        onClick = onClick
                    )
                }
                "analysis" -> {

                }
                "search" -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.arrow_side_line_left),
                            contentDescription = "Back",
                            modifier = modifier
                                .clickable { onNavigateToPage("home") }
                        )
                        val textStyle = TextStyle(
                            textAlign = TextAlign.Right,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        LogoContainer(textStyle)
                    }
                }
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
    textStyle: TextStyle = interTextStyle,
    modifier: Modifier = Modifier
) {
    when (pageContent) {
        "home" -> {
            when (content) {
                "all" -> {
                    QuickInfoCard()
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
                    RecentTransactionList(filterType = null)

                }

                "daily" -> {

                    var transactionType: String? by remember { mutableStateOf(null) }

                    DateNavBar()
                    IncomeExpenseNavBar(
                        activeTab = transactionType,
                        onTabSelected = { newType ->
                            transactionType = newType
                        }
                    )
                    RecentTransactionList(filterType = transactionType)
                }

                "monthly" -> {
                    DateNavBar()
                    MonthlyList()
                }
            }
        }
    }
}

@Composable
fun BottomNav(
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(BottomNavColor)
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.home_icon),
            contentDescription = null,
            modifier = modifier
                .clickable { onClick("all") }
        )
        Image(
            painter = painterResource(R.drawable.plus_icon),
            contentDescription = null,
            modifier = modifier
        )
        Image(
            painter = painterResource(R.drawable.insight_icon),
            contentDescription = null,
            modifier = modifier
        )
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
                    text = "Rp.${AvailableBalance()}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun Spendoo() {
    TransactionData(25)
    Pages()
}