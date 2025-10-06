package com.example.spendoov2.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

sealed interface CustomColor{
    data class Solid(val color: Color) : CustomColor
    data class Gradient(val brush: Brush) : CustomColor
}
