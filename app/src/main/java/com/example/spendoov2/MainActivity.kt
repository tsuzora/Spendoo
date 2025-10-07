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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
import com.example.spendoov2.ui.theme.CustomColor
import com.example.spendoov2.ui.theme.GreenDark
import com.example.spendoov2.ui.theme.GreenLight
import com.example.spendoov2.ui.theme.GreenMid
import com.example.spendoov2.ui.theme.GreyDark
import com.example.spendoov2.ui.theme.MainBackgroundColor
import com.example.spendoov2.ui.theme.SpendooV2Theme
import com.example.spendoov2.ui.theme.interFamily
import com.example.spendoov2.ui.theme.interTextStyle
import com.example.spendoov2.ui.theme.poppinsTextStyle
import com.example.spendoov2.ui.theme.unboundedFamily
import java.text.NumberFormat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendooV2Theme {
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(300.dp)
                ) {
                    Spendoo()
                }

            }
        }
    }
}

@Composable
fun Pages(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var pageType by remember { mutableStateOf("home") }
    var contentType by remember { mutableStateOf("all") }
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
                navController = navController)
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
                                    .clickable { navController.navigate("search_screen") }
                            )
                            Image(
                                painter = painterResource(R.drawable.dot_option),
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = modifier.padding(12.dp))
                    NavBarHome(
                        currentTab = contentTabs,
                        onClick = onClick
                    )
                }

                "search" -> {

                }

                "edit" -> {

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

@Composable
fun Spendoo() {
    TransactionData(25)
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home_screen") {

        composable("home_screen") {

            Pages(navController = navController)
        }
        composable("search_screen") {
            SearchPage(navController = navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePagePreview() {
    SpendooV2Theme {
        Spendoo()
    }
}
