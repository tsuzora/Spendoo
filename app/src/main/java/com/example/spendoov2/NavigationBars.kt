package com.example.spendoov2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spendoov2.ui.theme.GreenDark
import com.example.spendoov2.ui.theme.GreenLight


@Composable
fun NavBarHome(
    currentTab: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier) {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
    ) {
        NavTabHome(
            tabText = "All",
            isActive = currentTab == "all",
            onClick = { onClick("all") }
        )
        NavTabHome(
            tabText = "Daily",
            isActive = currentTab == "daily",
            onClick = { onClick("daily") }
        )
        NavTabHome(
            tabText = "Monthly",
            isActive = currentTab == "monthly",
            onClick = { onClick("monthly") }
        )
    }
}

@Composable
fun NavTabHome(
    tabText: String,
    isActive: Boolean,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(100.dp)
            .height(52.dp)
            .clickable { onClick("monthly") }
    ) {
        Text(
            text = tabText
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    if (isActive) {
                        GreenLight
                    } else {
                        GreenDark
                    }
                )
        ) {}
    }
}