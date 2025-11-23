package com.example.spendoov2.ui.theme

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val GreenLight = Color(0xFF45B065)

val GreenMid = Color(0xFF178237)
val GreenDark= Color(0xff0B4D1F)
val GreyDark = Color(0xff4A4F4B)
val MainBackgroundColor = Brush.linearGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFF178237),
        0.25f to Color(0xFF1B1C28),
        1f to Color(0xFF1B1C28)
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)
val BottomNavColor: Brush = Brush.horizontalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFF1B1C28),
        0.6f to Color(0xFF178237),
        1f to Color(0xFF178237)
    )
)
val CardInfoBackgroundColor = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xff0B4D1F),
        1f to Color(0xFF468D60).copy(alpha = 0.8f)
    ),
)

val ExpenseBackgroundColor = Brush.linearGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFFFF6A6A),
        1f to Color(0xFFFFBDBD)
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)
val IncomeBackgroundColor = Brush.linearGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFFA5FF6A),
        1f to Color(0xFFD2FFBD)
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)

val AddIncomeBGColor = Brush.linearGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFF2F9944),
        1f to Color(0xFF54E871)
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)

val AddExpenseBGColor = Brush.linearGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFFFF6A6A),
        1f to Color(0xFF994040)
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)



