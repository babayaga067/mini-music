package com.example.sangeet

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        val name = findViewById<EditText>(R.id.etName)
        val phone = findViewById<EditText>(R.id.etPhone)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val confirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val checkbox = findViewById<CheckBox>(R.id.checkboxTerms)
        val signupButton = findViewById<Button>(R.id.btnSignUp)
        val loginText = findViewById<TextView>(R.id.tvLogin)

        signupButton.setOnClickListener {
            if (!checkbox.isChecked) {
                Toast.makeText(this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.text.toString() != confirmPassword.text.toString()) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
        }

        loginText.setOnClickListener {
            Toast.makeText(this, "Redirecting to Login", Toast.LENGTH_SHORT).show()
        }
    }
}
