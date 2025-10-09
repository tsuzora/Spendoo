package com.example.spendoov2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.painterResource
import com.example.spendoov2.R

@Composable
fun AnalyzeHeaderSection(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit,
    month: String,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0E2E1E),
            Color(0xFF01220F)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = gradient)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🟢 APP TITLE
        Text(
            text = "SPENDOO",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "You Earn, We Learn",
            fontSize = 13.sp,
            color = Color(0xFFB8FFCD)
        )

        Spacer(Modifier.height(20.dp))

        // 🟢 TAB SWITCH: Statistics | Advice
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val tabs = listOf("Statistics", "Advice")
            tabs.forEach { tab ->
                Text(
                    text = tab,
                    color = if (selectedTab == tab) Color(0xFF54FFB0) else Color.White,
                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 6.dp)
                )
            }
        }

        Divider(
            color = Color(0xFF54FFB0),
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(Modifier.height(8.dp))

        // 🟢 PERIOD SELECTOR: Daily | Monthly | Yearly
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val periods = listOf("Daily", "Monthly", "Yearly")
            periods.forEach { period ->
                val isSelected = selectedPeriod == period
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .background(
                            color = if (isSelected) Color(0xFF00FF80) else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onPeriodSelected(period) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = period,
                        color = if (isSelected) Color.Black else Color.White,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // 🟢 MONTH NAVIGATION
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onPrevMonth() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Previous month",
                    tint = Color.White
                )
            }

            Text(
                text = month.uppercase(),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = { onNextMonth() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Next month",
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}
