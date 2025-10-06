package com.example.spendoov2.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import com.example.spendoov2.R


//Unbounded Font Family
val unboundedFamily = FontFamily(
    Font(R.font.unbounded)
)
val unboundTextStyle = TextStyle(
    color = Color.White,
    fontFamily = unboundedFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
    )
//Inter Font Family
val interFamily = FontFamily(
    Font(R.font.inter)
)
val interTextStyle = TextStyle(
    color = Color.White,
    fontFamily = interFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp
)
// Poppins (SemiBold) Font Family
val poppinsFamily = FontFamily(
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal)
)

val poppinsTextStyle = TextStyle(
    color = Color.White,
    fontFamily = poppinsFamily,
    fontSize = 10.sp,
    textAlign = TextAlign.Center,

)

