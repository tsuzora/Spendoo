package com.example.spendoov2

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawBehind
import com.example.spendoov2.ui.theme.*


val GreenDark = Color(0xFF1B5E20)
val GreenMid = Color(0xFF081C30)
val GreenLight = Color(0xFF4CAF50)
val MainBackgroundColor = Color(0xFF081C30)
@Composable
fun SpendooStatisticsPage(modifier: Modifier = Modifier) {
    val darkGreen = GreenDark
    val mediumGreen = GreenMid
    val lightGreen = GreenLight
    val bgDark = MainBackgroundColor

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // ✅ Background gradient sudah benar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            darkGreen,
                            mediumGreen.copy(alpha = 0.8f),
                            mediumGreen.copy(alpha = 0.5f),
                            bgDark,
                            bgDark,
                            bgDark
                        )
                    )
                )
        )
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Column(
                modifier = modifier.weight(1f)
            ) {
                // Top Banner
                StatisticsTopBanner()

                // Content
                StatisticsContent()
            }

            // Bottom Navigation
            BottomNav(
                onClick = { _ -> },
                onNavigateToPage = { _ -> }
            )
        }
    }
}

@Composable
fun StatisticsTopBanner(
    textStyle: TextStyle = interTextStyle,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalTextStyle provides textStyle) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp, 32.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.arrow_side_line_left),
                        contentDescription = "Back",
                        modifier = modifier.clickable { }
                    )
                    Spacer(modifier = modifier.width(12.dp))
                    LogoContainer(textStyle)
                }
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
                        modifier = modifier.clickable { }
                    )
                    Image(
                        painter = painterResource(R.drawable.dot_option),
                        contentDescription = null,
                        modifier = modifier.clickable { }
                    )
                }
            }

            Spacer(modifier = modifier.padding(12.dp))

            // Tab Navigation
            StatisticsNavBar()
        }
    }
}

@Composable
fun StatisticsNavBar(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf("statistics") }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { selectedTab = "statistics" }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Statistics",
                        fontSize = 16.sp,
                        color = if (selectedTab == "statistics") Color.White else Color.White.copy(alpha = 0.6f),
                        fontWeight = if (selectedTab == "statistics") FontWeight.Bold else FontWeight.Normal
                    )
                    if (selectedTab == "statistics") {
                        Spacer(modifier = modifier.height(4.dp))
                        Box(
                            modifier = modifier
                                .height(2.dp)
                                .width(80.dp)
                                .background(GreenLight)
                        )
                    }
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { selectedTab = "advice" }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Advice",
                        fontSize = 16.sp,
                        color = if (selectedTab == "advice") Color.White else Color.White.copy(alpha = 0.6f),
                        fontWeight = if (selectedTab == "advice") FontWeight.Bold else FontWeight.Normal
                    )
                    if (selectedTab == "advice") {
                        Spacer(modifier = modifier.height(4.dp))
                        Box(
                            modifier = modifier
                                .height(2.dp)
                                .width(60.dp)
                                .background(GreenLight)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatisticsContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp, 0.dp)
    ) {
        CompositionLocalProvider(LocalTextStyle provides poppinsTextStyle) {
            // Period Selector
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var selectedPeriod by remember { mutableStateOf("daily") }

                PeriodButton(
                    text = "Daily",
                    isSelected = selectedPeriod == "daily",
                    onClick = { selectedPeriod = "daily" },
                    modifier = Modifier.weight(1f)
                )
                PeriodButton(
                    text = "Monthly",
                    isSelected = selectedPeriod == "monthly",
                    onClick = { selectedPeriod = "monthly" },
                    modifier = Modifier.weight(1f)
                )
                PeriodButton(
                    text = "Yearly",
                    isSelected = selectedPeriod == "yearly",
                    onClick = { selectedPeriod = "yearly" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = modifier.height(24.dp))

            // Total Income
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Income",
                    color = GreenLight,
                    fontSize = 14.sp
                )
                Text(
                    text = "Rp4.038.000",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = modifier.height(16.dp))

            // Income/Expense Tabs
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var selectedType by remember { mutableStateOf("income") }

                Column(
                    modifier = modifier
                        .weight(1f)
                        .clickable { selectedType = "income" },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Income",
                        color = if (selectedType == "income") GreenLight else Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        fontWeight = if (selectedType == "income") FontWeight.Bold else FontWeight.Normal
                    )
                    if (selectedType == "income") {
                        Spacer(modifier = modifier.height(4.dp))
                        Box(
                            modifier = modifier
                                .height(2.dp)
                                .fillMaxWidth()
                                .background(GreenLight)
                        )
                    }
                }

                Column(
                    modifier = modifier
                        .weight(1f)
                        .clickable { selectedType = "expense" },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Expense",
                        color = if (selectedType == "expense") Color.Red else Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        fontWeight = if (selectedType == "expense") FontWeight.Bold else FontWeight.Normal
                    )
                    if (selectedType == "expense") {
                        Spacer(modifier = modifier.height(4.dp))
                        Box(
                            modifier = modifier
                                .height(2.dp)
                                .fillMaxWidth()
                                .background(Color.Red)
                        )
                    }
                }
            }

            Spacer(modifier = modifier.height(24.dp))

            // Chart Area
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .drawBehind {
                        val width = size.width
                        val height = size.height

                        val path = Path().apply {
                            moveTo(0f, height * 0.6f)
                            cubicTo(
                                width * 0.15f, height * 0.7f,
                                width * 0.25f, height * 0.5f,
                                width * 0.35f, height * 0.4f
                            )
                            cubicTo(
                                width * 0.45f, height * 0.3f,
                                width * 0.55f, height * 0.8f,
                                width * 0.65f, height * 0.7f
                            )
                            cubicTo(
                                width * 0.75f, height * 0.6f,
                                width * 0.85f, height * 0.2f,
                                width, height * 0.15f
                            )
                            lineTo(width, height)
                            lineTo(0f, height)
                            close()
                        }

                        drawPath(
                            path = path,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF4CAF50).copy(alpha = 0.6f),
                                    Color(0xFF2E7D32).copy(alpha = 0.3f),
                                    Color(0xFF1B5E20).copy(alpha = 0.1f)
                                )
                            )
                        )
                    }
            )

            // Days of week
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                    Text(
                        text = day,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PeriodButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) GreenLight else Color.Transparent,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        border = if (!isSelected)
            androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
        else null,
        modifier = modifier.height(40.dp)
    ) {
        Text(
            text = text,
            style = poppinsTextStyle,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSpendooStatisticsPage() {
    SpendooV2Theme {
        SpendooStatisticsPage()
    }
}
