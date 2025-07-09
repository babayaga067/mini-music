package com.example.sangeet.model.auth

data class SignupRequest(
    val fullName: String,
    val email: String,
    val password: String
)
