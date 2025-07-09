package com.example.sangeet.repository

import com.example.sangeet.model.UserModel
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val client: Client,
    private val databaseId: String,
    private val collectionId: String
) : UserRepository {

    private val account = Account(client)
    private val databases = Databases(client)

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                account.createEmailSession(email, password)
                callback(true, "Login successful")
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Login failed")
            }
        }
    }

    override fun register(
        fullName: String,
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = fullName // passing fullname to appwrite
                )
                callback(true, "Registration successful", user.id)
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Registration failed", "")
            }
        }
    }

    override fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                databases.createDocument(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    documentId = userId,
                    data = mapOf(
                        "userId" to userModel.userId,
                        "fullName" to userModel.fullName,
                        "email" to userModel.email
                    ),
                    permissions = listOf(
                        Permission.read(Role.user(userId)),
                        Permission.write(Role.user(userId))
                    )
                )
                callback(true, "User added to database")
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Failed to add user")
            }
        }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                account.createRecovery(email, "https://your-app-url.com/recovery")
                callback(true, "Password reset link sent")
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Failed to send reset link")
            }
        }
    }

    override fun updatePassword(newPassword: String, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                account.updatePassword(password = newPassword)
                callback(true, "Password updated")
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Failed to update password")
            }
        }
    }

    override fun getUserById(userId: String, callback: (UserModel?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val doc = databases.getDocument(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    documentId = userId
                )
                val user = UserModel(
                    userId = doc.data["userId"] as String,
                    fullName = doc.data["fullName"] as String,
                    email = doc.data["email"] as String
                )
                callback(user)
            } catch (e: AppwriteException) {
                callback(null)
            }
        }
    }

    override fun updateProfile(userId: String, updates: Map<String, Any?>, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                databases.updateDocument(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    documentId = userId,
                    data = updates
                )
                callback(true, "Profile updated")
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Failed to update profile")
            }
        }
    }

    override fun getCurrentUser(callback: (UserModel?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = account.get()
                val userModel = UserModel(
                    userId = user.id,
                    fullName = user.name,
                    email = user.email
                )
                callback(userModel)
            } catch (e: AppwriteException) {
                callback(null)
            }
        }
    }

    override fun logout(callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                account.deleteSessions()
            } catch (_: AppwriteException) {
                // Ignore errors on logout
            } finally {
                callback()
            }
        }
    }
}