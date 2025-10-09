package com.example.spendoov2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.ui.theme.interFamily
import com.example.spendoov2.ui.theme.unboundedFamily
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Navigate to login after 2.5 seconds
    LaunchedEffect(Unit) {
        delay(2500L)
        onNavigateToLogin()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B1C28),
                        Color(0xFF178237)
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            // Logo Image - Replace with your actual logo
            Image(
                painter = painterResource(R.drawable.spendoobg), // Replace with your logo
                contentDescription = "Spendoo Logo",
                modifier = Modifier.size(200.dp)
            )

            Text(
                text = "SPENDOO",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = unboundedFamily,
                color = Color.White
            )

            Text(
                text = "You Earn, We Learn",
                fontSize = 16.sp,
                fontFamily = interFamily,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        onNavigateToLogin = {}
    )
}