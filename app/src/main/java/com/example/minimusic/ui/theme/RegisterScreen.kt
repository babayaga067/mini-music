package com.example.registerapp

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current //curent context

    // Form state
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val countries = listOf("India", "USA", "UK", "Germany", "Canada")


    var dob by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Register", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Firstname & Lastname fields
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Firstname") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Lastname") },
                modifier = Modifier.weight(1f)
            )
        }

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // Country Dropdown
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) {
            OutlinedTextField(
                value = selectedCountry,
                onValueChange = {},
                label = { Text("Select Country") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        modifier = Modifier.clickable { expanded = true }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(text = country) },
                        onClick = {
                            selectedCountry = country
                            expanded = false
                        }
                    )
                }
            }
        }

        // DOB field
        OutlinedTextField(
            value = dob,
            onValueChange = { dob = it },
            label = { Text("DOB (dd/mm/yyyy)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // Gender selection
        Text("Gender", modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))
        Row {
            listOf("Male", "Female", "Others").forEach { gender ->
                Row(modifier = Modifier.padding(end = 16.dp)) {
                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = { selectedGender = gender }
                    )
                    Text(gender, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        // Terms and Conditions
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it }
            )
            Text("I accept terms and conditions", modifier = Modifier.padding(start = 4.dp))
        }

        // Register Button
        Button(
            onClick = {
                if (firstName.isNotEmpty() &&
                    email.isNotEmpty() &&
                    selectedGender.isNotEmpty() &&
                    selectedCountry.isNotEmpty() &&
                    termsAccepted
                ) {
                    Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Register")
        }

        // Sign-in Text Link
        Text(
            text = "Already have Account? Signin",
            color = Color.Blue,
            modifier = Modifier
                .padding(top = 12.dp)
                .clickable {
                    Toast.makeText(context, "Redirect to Signin screen", Toast.LENGTH_SHORT).show()
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardApp() {
    RegisterScreen(PaddingValues(0.dp))
}