package com.example.sangeet.repository

import com.example.sangeet.model.UserModel

interface UserRepository {

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)

    fun register(
        fullName: String, // ✅ Add this
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    )

    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit)

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit)

    fun updatePassword(newPassword: String, callback: (Boolean, String) -> Unit)

    fun getUserById(userId: String, callback: (UserModel?) -> Unit)

    fun updateProfile(userId: String, updates: Map<String, Any?>, callback: (Boolean, String) -> Unit)

    fun getCurrentUser(callback: (UserModel?) -> Unit)

    fun logout(callback: () -> Unit)
}