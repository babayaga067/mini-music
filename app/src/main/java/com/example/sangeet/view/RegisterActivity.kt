package com.example.sangeet.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sangeet.viewmodel.UserViewModel
import com.example.sangeet.repository.UserRepositoryImpl
import com.example.sangeet.model.UserModel


class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterScreen()
        }
    }
}

@Composable
fun RegisterScreen(
    onSignupSuccess: () -> Unit = {}, // Callback when signup is successful
    onLoginClick: () -> Unit = {}     // Navigate to login
) {

    val context = LocalContext.current
    val activity = context as? Activity

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }

    val gradient = Brush.verticalGradient(
        listOf(Color(0xFF4A004A), Color(0xFF1C0038))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Sign Up", fontSize = 30.sp, color = Color.White)

            Spacer(modifier = Modifier.height(20.dp))

            RegisterTextField("Full Name", name) { name = it }
            RegisterTextField("Email", email, KeyboardType.Email) { email = it }
            RegisterTextField("Password", password, KeyboardType.Password, isPassword = true) { password = it }
            RegisterTextField("Confirm Password", confirmPassword, KeyboardType.Password, isPassword = true) { confirmPassword = it }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
                Text("I accept the Terms & Conditions", color = Color.White, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else if (!termsAccepted) {
                        Toast.makeText(context, "Please accept terms", Toast.LENGTH_SHORT).show()
                    } else if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    } else {
                        // Start registration process
                        userViewModel.register(email, password) { success, message, userId ->
                            if (success) {
                                val userModel = UserModel(
                                    userId, name, email, password
                                )
                                userViewModel.addUserToDatabase(
                                    userId,
                                    userModel
                                ) { dbSuccess, dbMessage ->
                                    if (dbSuccess) {
                                        Toast.makeText(context, "Signup successful!", Toast.LENGTH_LONG).show()
                                        val intent = Intent(context, LoginActivity::class.java)
                                        context.startActivity(intent)
                                    } else {
                                        Toast.makeText(context, "Database error: $dbMessage", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Registration failed: $message", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("SIGN UP", color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text("Already have an account? ", color = Color.White)
                TextButton(onClick = onLoginClick) {
                    Text("Login", color = Color.Cyan)
                }
            }
        }
    }
}

@Composable
fun RegisterTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(vertical = 4.dp)
    )
}

@Preview(showSystemUi = true)
@Composable
fun RegisterActivityPreview() {
    RegisterScreen()
}