package com.example.spendoov2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendoov2.ui.theme.GreenDark
import com.example.spendoov2.ui.theme.MainBackgroundColor
import com.example.spendoov2.ui.theme.interTextStyle


@Composable
fun SearchPage(
    navController: NavController,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(MainBackgroundColor)
    ) {
        Column(
            modifier = modifier
                .padding(24.dp, 32.dp)
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_side_line_left),
                    contentDescription = "Back",
                    modifier = modifier
                        .clickable { navController.navigate("home_screen") }
                )
                val textStyle = TextStyle(
                    textAlign = TextAlign.Right,
                    color = Color.White,
                    fontSize = 16.sp
                )
                LogoContainer(textStyle)
            }
            SearchBar()
            RecentSearches()
        }
    }
}
@Composable
fun SearchBar(modifier: Modifier = Modifier) {
        CompositionLocalProvider(LocalTextStyle provides interTextStyle) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .height(48.dp)
                    .background(Color(0xFFD9D9D9))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp, 0.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(GreenDark),
                        modifier = modifier.size(32.dp)
                    )
                    Text(
                        text = "Search",
                        color = Color(0xFF0B4D1F),
                        modifier = modifier
                            .fillMaxWidth()

                            .padding(16.dp, 0.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }


val recentSearches = mutableListOf<String>()

@Composable
fun RecentSearches(modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalTextStyle provides interTextStyle) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Recent:"
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 16.dp)
            ) {
                Text(
                    text = "Total Income 15 August"
                )
                Text(
                    text = "Total Income 12 August"
                )
                Text(
                    text = "Total Expense 15 August"
                )
            }
            Box(
                modifier = modifier
                    .width(300.dp)
                    .height(6.dp)
                    .background(Color(0xFFD9D9D9))
            )
        }
    }
}