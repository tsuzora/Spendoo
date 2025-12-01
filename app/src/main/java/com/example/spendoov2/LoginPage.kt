package com.example.spendoov2

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.data.LocalData
import com.example.spendoov2.ui.theme.interFamily
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider

// GANTI INI DENGAN "WEB CLIENT ID" DARI FIREBASE CONSOLE -> AUTHENTICATION -> GOOGLE -> WEB SDK CONFIG
// JANGAN PAKAI ANDROID CLIENT ID
const val WEB_CLIENT_ID = "766263715089-7pfid4burqhqhh3tdtl0pa5mtai8klni.apps.googleusercontent.com"

@Composable
fun LoginPage(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) } // State loading (opsional untuk UI)

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // --- LOGIKA GOOGLE SIGN-IN ---

    // 1. Setup Google Client
    val googleSignInClient = remember {
        getGoogleSignInClient(context, WEB_CLIENT_ID)
    }

    // 2. Launcher untuk Intent Google Sign In
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                isLoading = true
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleGoogleSignInResult(
                    task = task,
                    auth = auth,
                    onSuccess = {
                        isLoading = false
                        Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                        onNavigateToHome()
                    },
                    onError = { errorMsg ->
                        isLoading = false
                        errorMessage = errorMsg
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                    }
                )
            } else {
                errorMessage = "Login Google dibatalkan atau gagal (Result Code: ${result.resultCode})"
            }
        }
    )

    // --- LOGIKA GITHUB SIGN-IN ---
    val githubProvider = remember {
        OAuthProvider.newBuilder("github.com").build()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF178237),
                        Color(0xFF1B1C28)
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // --- Form Email ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = "E-Mail",
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Form Password ---
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        text = "Password",
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Tombol Log In ---
            Button(
                onClick = {
                    errorMessage = null
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Email dan Password harus diisi."
                    } else {
                        isLoading = true
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
                                    onNavigateToHome()
                                } else {
                                    Log.w("Login", "signInWithEmail:failure", task.exception)
                                    errorMessage = "Login gagal: ${task.exception?.localizedMessage}"
                                }
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = if (isLoading) "Loading..." else "Log In",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 64.dp, vertical = 8.dp)
                )
            }

            // --- Tampilan Error Message ---
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Links ---
            Text(
                text = "Forgot Password?",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontFamily = interFamily,
                modifier = Modifier.clickable { onNavigateToForgotPassword() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sign Up",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = interFamily,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Continue as Guest",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = interFamily,
                modifier = Modifier.clickable {
                    LocalData.Name = initGuest()
                    onNavigateToHome()
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "or login with",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontFamily = interFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Social Login Row ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ikon Google
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Login with Google",
                    modifier = Modifier
                        .size(52.dp)
                        .clickable {
                            errorMessage = null
                            // Pastikan logout dulu dari Google Client agar bisa memilih akun lagi
                            googleSignInClient.signOut().addOnCompleteListener {
                                val signInIntent = googleSignInClient.signInIntent
                                googleSignInLauncher.launch(signInIntent)
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

// --- FUNGSI HELPER UNTUK GOOGLE SIGN-IN ---

private fun getGoogleSignInClient(
    context: android.content.Context,
    webClientId: String
): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(webClientId)
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, gso)
}

private fun handleGoogleSignInResult(
    task: Task<GoogleSignInAccount>,
    auth: FirebaseAuth,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    try {
        val account = task.getResult(ApiException::class.java)
        if (account != null) {
            val idToken = account.idToken
            if (idToken != null) {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                Log.d("GoogleSignIn", "Mencoba Auth Firebase dengan token Google...")

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { firebaseTask ->
                        if (firebaseTask.isSuccessful) {
                            Log.d("GoogleSignIn", "Login Firebase SUKSES")
                            onSuccess()
                        } else {
                            val exception = firebaseTask.exception
                            Log.e("GoogleSignIn", "Login Firebase GAGAL", exception)
                            // Pesan error yang lebih user-friendly
                            onError("Gagal autentikasi Firebase: ${exception?.message}")
                        }
                    }
            } else {
                onError("Gagal mendapatkan ID Token dari Google (Token null).")
            }
        } else {
            onError("Akun Google tidak ditemukan.")
        }
    } catch (e: ApiException) {
        Log.e("GoogleSignIn", "Google Sign In API Error: code ${e.statusCode}", e)
        // Code 12500 biasanya berarti SHA-1 Fingerprint belum ditambahkan di Firebase Console
        // Code 12501 berarti user membatalkan dialog sign in
        // Code 10 berarti salah konfigurasi (misal client ID salah)
        val readableError = when(e.statusCode) {
            12500 -> "Konfigurasi Salah: SHA-1 Fingerprint belum ditambahkan di Firebase Console."
            12501 -> "Login dibatalkan oleh pengguna."
            10 -> "Kesalahan Konfigurasi Developer (Cek Client ID)."
            else -> "Google Sign In Error: ${e.statusCode}"
        }
        onError(readableError)
    } catch (e: Exception) {
        Log.e("GoogleSignIn", "Unknown Error", e)
        onError("Terjadi kesalahan tak terduga: ${e.message}")
    }
}

// --- PREVIEW SECTION ---

@Preview
@Composable
fun LoginPagePreview() {
    LoginPage(
        onNavigateToHome = {},
        onNavigateToSignUp = {},
        onNavigateToForgotPassword = {}
    )
}