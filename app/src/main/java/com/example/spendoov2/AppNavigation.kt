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

    // NavController tidak lagi dibutuhkan di sini karena navigasi utama
    // ditangani di dalam Spendoo Composable.
    // val navController = rememberNavController()

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
                onNavigateToForgotPassword = { currentScreen = "forgot_password" }
            )
        }
        "signup" -> {
            SignUpPage(
                onNavigateBack = { currentScreen = "login" }
            )
        }
        "home" -> {
            // Saat logout, kembali ke layar login
            Spendoo(
                onLogout = { currentScreen = "login" }
            )
        }
        "forgot_password" -> {
            ForgotPasswordPage(
                onNavigateBack = { currentScreen = "login" }
            )
        }
    }
}


//@Composable
//fun AppNavigation(modifier: Modifier = Modifier) {
//    var currentScreen by remember { mutableStateOf("splash") }
//    val navController = rememberNavController()
//
//    when (currentScreen) {
//        "splash" -> {
//            SplashScreen(
//                onNavigateToLogin = { currentScreen = "login" }
//            )
//        }
//        "login" -> {
//            LoginPage(
//                onNavigateToHome = { currentScreen = "home" },
//                onNavigateToSignUp = { currentScreen = "signup" }
//            )
//        }
//        "signup" -> {
//            SignUpPage(
//                onNavigateBack = { currentScreen = "login" }
//            )
//        }
//        "home" -> {
//            Spendoo(
//                onLogout = {
//                    navController.navigate("login") {
//                        popUpTo("home") { inclusive = true }
//                    }
//                }
//            )
//        }
//
//    }
//}