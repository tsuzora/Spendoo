package com.example.spendoov2

import AddTransaction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf("splash") }

    when (currentScreen) {
        "splash" -> {
            SplashScreen(
                onNavigateToLogin = { currentScreen = "login" }
            )
        }
        "login" -> {
            LoginPage(
                onNavigateToHome = { currentScreen = "home" },
                onNavigateToSignUp = { currentScreen = "signup" }
            )
        }
        "signup" -> {
            SignUpPage(
                onNavigateBack = { currentScreen = "login" }
            )
        }
        "home" -> {
            Spendoo()
        }

    }
}