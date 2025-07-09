package com.example.sangeet.model

data class UserModel(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",

    //avoid using password field here - use a separate LoginRequest or SignupRequest DTO.
)
