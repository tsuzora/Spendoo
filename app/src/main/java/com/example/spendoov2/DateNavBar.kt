package com.example.spendoov2


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.ui.theme.poppinsFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DateNavBar(
    currentDate: LocalDate,
    onDateClick: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    isMonthSelector: Boolean = false, // Flag untuk membedakan mode harian/bulanan
    modifier: Modifier = Modifier
) {
    val formatter = if (isMonthSelector) {
        DateTimeFormatter.ofPattern("MMMM, yyyy", Locale.ENGLISH)
    } else {
        DateTimeFormatter.ofPattern("dd MMMM, yyyy", Locale.ENGLISH)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(28.dp, 12.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.arrow_side_line_left),
            contentDescription = "Previous",
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onPrevious)
        )
        Text(
            text = currentDate.format(formatter),
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.clickable(onClick = onDateClick)
        )
        Image(
            painter = painterResource(R.drawable.arrow_side_line_right),
            contentDescription = "Next",
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onNext)
        )
    }
}


//@Composable
//fun DateNavBar(
//    modifier: Modifier = Modifier) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(28.dp, 0.dp)
//    ) {
//        Image(
//            painter = painterResource(R.drawable.arrow_side_line_left),
//            contentDescription = null
//        )
//        Text(
//            text = "September, 2025",
//            fontFamily = poppinsFamily,
//            color = Color.White
//        )
//        Image(
//            painter = painterResource(R.drawable.arrow_side_line_right),
//            contentDescription = null
//        )
//    }
//}