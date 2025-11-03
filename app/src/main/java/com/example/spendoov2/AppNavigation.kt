package com.example.spendoov2


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    // State untuk mengelola layar saat ini (Splash, Login, SignUp, Home)
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
                onNavigateToSignUp = { currentScreen = "signup" },
                // Ini memanggil halaman yang baru Anda buat
                onNavigateToForgotPassword = { currentScreen = "forgot_password" }
            )
        }
        "signup" -> {
            SignUpPage(
                onNavigateBack = { currentScreen = "login" }
            )
        }
        // Ini adalah case baru untuk halaman forgot password
        "forgot_password" -> {
            ForgotPasswordPage(
                onNavigateBack = { currentScreen = "login" }
            )
        }
        "home" -> {
            // Saat logout, kembali ke layar login
            Spendoo(
                onLogout = { currentScreen = "login" }
            )
        }
    }
}