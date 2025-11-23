package com.example.spendoov2

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendoov2.ui.theme.interFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest // <-- Make sure this import is present

@Composable
fun SignUpPage(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var userName by remember { mutableStateOf("") } // <-- Changed from fullName
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null)}

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
        // Close Button
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
                .size(32.dp)
                .clickable { onNavigateBack() }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            // Sign Up Title
            Text(
                text = "Sign Up",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = interFamily
            )

            Text(
                text = "Fill in the form to create an account",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = interFamily,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // User Name TextField
            OutlinedTextField(
                value = userName, // <-- Changed
                onValueChange = { userName = it }, // <-- Changed
                placeholder = {
                    Text(
                        text = "Username",
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
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email TextField
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

            Spacer(modifier = Modifier.height(20.dp))

            // Password TextField
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password TextField
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = {
                    Text(
                        text = "Confirm Password",
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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Button
            Button(
                onClick = {
                    errorMessage = null
                    // 1. Local validation
                    if (password != confirmPassword) {
                        errorMessage = "Password and Confirm Password do not match."
                    } else if (userName.isBlank() || email.isBlank() || password.isBlank()) { // <-- Changed
                        errorMessage = "All fields are required."
                    } else {
                        // 2. Try to create account on Firebase
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // 3. ACCOUNT CREATED SUCCESSFULLY
                                    //    Now, update the user's profile with the Username
                                    Log.d("SignUp", "Account created successfully. Saving profile...")
                                    val user = auth.currentUser

                                    if (user != null) {
                                        val profileUpdates = UserProfileChangeRequest.Builder()
                                            .setDisplayName(userName) // <-- Changed
                                            .build()

                                        user.updateProfile(profileUpdates)
                                            .addOnCompleteListener { profileTask ->
                                                if (profileTask.isSuccessful) {
                                                    Log.d("SignUp", "User profile updated successfully.")
                                                } else {
                                                    // Failed to update profile, but account was created.
                                                    // Just log the error.
                                                    Log.w("SignUp", "Error updating profile.", profileTask.exception)
                                                }
                                                // 4. Navigate back (after profile update is attempted)
                                                onNavigateBack()
                                            }
                                    } else {
                                        // If user is null (shouldn't happen), navigate back immediately
                                        onNavigateBack()
                                    }

                                } else {
                                    // 4. Handle errors from Firebase
                                    val exception = task.exception
                                    errorMessage = when {
                                        exception?.message?.contains("email address is already in use") == true ->
                                            "This email address is already in use."
                                        exception?.message?.contains("Weak Password") == true ->
                                            "Password is too weak. (Minimum 6 characters)"
                                        else ->
                                            task.exception?.message ?: "Sign up failed. Please try again."
                                    }
                                    Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                                }
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Sign Up",
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
                    color = Color.Red, // Use an error color
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SignUpPagePreview() {
    SignUpPage(
        onNavigateBack = {}
    )
}


//package com.example.spendoov2
//
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.spendoov2.ui.theme.interFamily
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.UserProfileChangeRequest
//
//@Composable
//fun SignUpPage(
//    onNavigateBack: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    var fullName by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf<String?>(null)}
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF1B1C28),
//                        Color(0xFF178237)
//                    )
//                )
//            )
//    ) {
//        // Close Button
//        Icon(
//            imageVector = Icons.Default.Close,
//            contentDescription = "Close",
//            tint = Color.White,
//            modifier = Modifier
//                .align(Alignment.TopStart)
//                .padding(24.dp)
//                .size(32.dp)
//                .clickable { onNavigateBack() }
//        )
//
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(32.dp)
//        ) {
//            // Sign Up Title
//            Text(
//                text = "Sign Up",
//                color = Color.White,
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold,
//                fontFamily = interFamily
//            )
//
//            Text(
//                text = "Fill in the form to create an account",
//                color = Color.White,
//                fontSize = 14.sp,
//                fontFamily = interFamily,
//                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
//            )
//
//            // Full Name TextField
//            OutlinedTextField(
//                value = fullName,
//                onValueChange = { fullName = it },
//                placeholder = {
//                    Text(
//                        text = "Username",
//                        color = Color.White.copy(alpha = 0.7f),
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
//                ),
//                singleLine = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 24.dp)
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Email TextField
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                placeholder = {
//                    Text(
//                        text = "E-Mail",
//                        color = Color.White.copy(alpha = 0.7f),
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
//                ),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                singleLine = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 24.dp)
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Password TextField
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                placeholder = {
//                    Text(
//                        text = "Password",
//                        color = Color.White.copy(alpha = 0.7f),
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
//                ),
//                visualTransformation = PasswordVisualTransformation(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                singleLine = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 24.dp)
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Confirm Password TextField
//            OutlinedTextField(
//                value = confirmPassword,
//                onValueChange = { confirmPassword = it },
//                placeholder = {
//                    Text(
//                        text = "Confirm Password",
//                        color = Color.White.copy(alpha = 0.7f),
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
//                ),
//                visualTransformation = PasswordVisualTransformation(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                singleLine = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 24.dp)
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Sign Up Button
//            Button(
//                onClick = {
//                    errorMessage = null
//                    // Validate and navigate back to login
//                    if (password != confirmPassword) {
//                        errorMessage = "Password dan Konfirmasi Password tidak cocok."
//                    } else if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
//                        errorMessage = "Semua kolom harus diisi."
//                    } else {
//                        // 3. Jika lolos validasi lokal, coba Firebase
//                        auth.createUserWithEmailAndPassword(email, password)
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    onNavigateBack()
//                                } else {
//                                    // 4. Tangani error dari Firebase
//                                    val exception = task.exception
//                                    errorMessage = when {
//                                        exception?.message?.contains("email address is already in use") == true ->
//                                            "Email ini sudah terdaftar."
//                                        exception?.message?.contains("Weak Password") == true ->
//                                            "Password terlalu lemah. (Minimal 6 karakter)"
//                                        else ->
//                                            task.exception?.message ?: "Sign up gagal. Coba lagi."
//                                    }
//                                    Log.w("SignUp", "createUserWithEmail:failure", task.exception)
//                                }
//                            }
//                    }
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White
//                ),
//                shape = RoundedCornerShape(50),
//                modifier = Modifier.padding(horizontal = 24.dp)
//            ) {
//                Text(
//                    text = "Sign Up",
//                    color = Color.Black,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    modifier = Modifier.padding(horizontal = 64.dp, vertical = 8.dp)
//                )
//            }
//            if (errorMessage != null) {
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = errorMessage!!,
//                    color = Color.Red, // Gunakan warna error
//                    fontSize = 14.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.padding(horizontal = 24.dp)
//                )
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun SignUpPagePreview() {
//    SignUpPage(
//        onNavigateBack = {}
//    )
//}
//
