package com.example.spendoov2


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spendoov2.ui.theme.poppinsFamily


@Composable
fun DateNavBar(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(28.dp, 4.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.arrow_side_line_left),
            contentDescription = null
        )
        Text(
            text = "August 15, 2025",
            fontFamily = poppinsFamily,
            color = Color.White
        )
        Image(
            painter = painterResource(R.drawable.arrow_side_line_right),
            contentDescription = null
        )
    }
}