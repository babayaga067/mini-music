package com.example.sangeet.repository

import com.example.sangeet.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    // Authentication
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun register(
        userId: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        gender: String,
        address: String,
        callback: (Boolean, String, String) -> Unit
    )
    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit)
    fun updatePassword(
        userId: String,
        oldPassword: String,
        newPassword: String,
        callback: (Boolean, String) -> Unit
    )
    fun logout(callback: () -> Unit)
    fun getCurrentUser(): FirebaseUser?

    // Database
    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit)
    fun getUserById(userId: String, callback: (UserModel?) -> Unit)
    fun updateProfile(userId: String, updates: Map<String, Any?>, callback: (Boolean, String) -> Unit)
}
