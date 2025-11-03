package com.example.spendoov2

import android.app.Activity
import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.ui.theme.interFamily
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider // <-- IMPORT BARU

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

    val context = LocalContext.current // <-- Ubah context ke Activity
    val auth = FirebaseAuth.getInstance()

    // --- LOGIKA GOOGLE SIGN-IN ---
    val googleSignInClient = remember { getGoogleSignInClient(context) }
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleGoogleSignInResult(
                    task = task,
                    auth = auth,
                    onSuccess = onNavigateToHome,
                    onError = { errorMsg -> errorMessage = errorMsg }
                )
            } else {
                errorMessage = "Login Google dibatalkan."
            }
        }
    )

    // --- LOGIKA GITHUB SIGN-IN ---
    val githubProvider = remember {
        OAuthProvider.newBuilder("github.com").build()
    }

    // --- AKHIR LOGIKA ---

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

            // ... (Email, Password, Tombol "Log In", dan Error Message) ...
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

            Button(
                onClick = {
                    errorMessage = null
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Email dan Password harus diisi."
                    } else {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onNavigateToHome()
                                } else {
                                    Log.w("Login", "signInWithEmail:failure", task.exception)
                                    errorMessage = "Login gagal. Periksa kembali email dan password Anda."
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
                    text = "Log In",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 64.dp, vertical = 8.dp)
                )
            }

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
                modifier = Modifier.clickable { onNavigateToHome() }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ikon Google
                Image(
                    painter = painterResource(id = R.drawable.google_icon), // GANTI NAMA INI
                    contentDescription = "Login with Google",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            errorMessage = null
                            val signInIntent = googleSignInClient.signInIntent
                            googleSignInLauncher.launch(signInIntent)
                        }
                )

                // Ikon Facebook
                Image(
                    painter = painterResource(id = R.drawable.facebook_icon), // GANTI NAMA INI
                    contentDescription = "Login with Facebook",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            errorMessage = "Login Facebook belum diimplementasikan."
                        }
                )

                // --- ⬇️ PERUBAHAN DI SINI ⬇️ ---
                // Ikon GitHub
                Image(
                    painter = painterResource(id = R.drawable.github_icon), // GANTI NAMA INI
                    contentDescription = "Login with GitHub",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            errorMessage = null

                            // 1. Dapatkan Activity secara aman DARI context
                            val activity = context as? Activity

                            // 2. Periksa apakah context-nya adalah Activity
                            if (activity != null) {
                                // Cek apakah ada aktivitas login lain yang sedang berjalan
                                val pendingResultTask = auth.pendingAuthResult
                                if (pendingResultTask != null) {
                                    // Ada login yang tertunda, coba selesaikan
                                    pendingResultTask.addOnSuccessListener {
                                        Log.d("GitHubSignIn", "Login tertunda SUKSES")
                                        onNavigateToHome()
                                    }.addOnFailureListener { e ->
                                        Log.w("GitHubSignIn", "Login tertunda GAGAL", e)
                                        errorMessage = "Login GitHub Gagal: ${e.message}"
                                    }
                                } else {
                                    // 3. ⬇️ INI BAGIAN PENTING ⬇️
                                    // Pastikan Anda memanggil ini dengan 'activity', BUKAN 'context'
                                    auth.startActivityForSignInWithProvider(activity, githubProvider)
                                        .addOnSuccessListener {
                                            Log.d("GitHubSignIn", "Alur web SUKSES, login...")
                                            onNavigateToHome()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w("GitHubSignIn", "Alur web GAGAL", e)
                                            errorMessage = "Login GitHub Gagal: ${e.message}"
                                        }
                                }
                            } else {
                                // Ini adalah fallback jika context bukan Activity
                                errorMessage = "Tidak bisa memulai login: Konteks aplikasi tidak valid."
                            }
                        }
                )
                // --- ⬆️ AKHIR PERUBAHAN ⬆️ ---

                // Ikon Apple
                Image(
                    painter = painterResource(id = R.drawable.apple_icon), // GANTI NAMA INI
                    contentDescription = "Login with Apple",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            errorMessage = "Login Apple belum diimplementasikan."
                        }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

// --- FUNGSI HELPER UNTUK GOOGLE SIGN-IN ---

private fun getGoogleSignInClient(context: android.content.Context): GoogleSignInClient {
    // ⚠️ PENTING: GANTI DENGAN WEB CLIENT ID ANDA
    val webClientId = "766263715089-7pfid4burqhqhh3tdtl0pa5mtai8klni.apps.googleusercontent.com"

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
        val account = task.getResult(ApiException::class.java)!!
        val idToken = account.idToken!!
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        Log.d("GoogleSignIn", "Firebase Auth dengan Google: ${account.id}")

        auth.signInWithCredential(credential)
            .addOnCompleteListener { firebaseTask ->
                if (firebaseTask.isSuccessful) {
                    Log.d("GoogleSignIn", "Login Firebase SUKSES")
                    onSuccess()
                } else {
                    Log.w("GoogleSignIn", "Login Firebase GAGAL", firebaseTask.exception)
                    onError("Login Firebase Gagal: ${firebaseTask.exception?.message}")
                }
            }
    } catch (e: ApiException) {
        Log.w("GoogleSignIn", "Login Google GAGAL", e)
        onError("Login Google Gagal: (code ${e.statusCode})")
    }
}

@Preview
@Composable
fun LoginPagePreview() {
    LoginPage(
        onNavigateToHome = {},
        onNavigateToSignUp = {},
        onNavigateToForgotPassword = {}
    )
}