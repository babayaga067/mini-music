package com.example.sangeet.repository

import com.example.sangeet.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepositoryImpl : UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("Users")

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login successful")
                } else {
                    callback(false, task.exception?.message ?: "Login failed")
                }
            }
    }

    override fun register(
        userid: String,
        email: String,
        password: String,
        firstname: String,
        lastname: String,

        gender: String,
        address: String,
        callback: (Boolean, String, String) -> Unit
    ) {

        val password = "$password" // ← replace this with actual password input

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid.orEmpty()
                    callback(true, "Register successful", uid)
                } else {
                    callback(false, task.exception?.message ?: "Registration failed", "")
                }
            }
    }

    override fun addUserToDatabase(
        userid: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userid).setValue(userModel).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "User added")
            } else {
                callback(false, task.exception?.message ?: "Failed to add user")
            }
        }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Reset email sent to $email successfully")
                } else {
                    callback(false, task.exception?.message ?: "Password reset failed")
                }
            }
    }

    override fun getUserById(
        userid: String,
        callback: (UserModel?) -> Unit
    ) {
        ref.child(userid).get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(UserModel::class.java)
            callback(user)
        }.addOnFailureListener {
            callback(null)
        }
    }

    override fun updateProfile(
        userId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).updateChildren(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Profile updated successfully")
                } else {
                    callback(false, task.exception?.message ?: "Update failed")
                }
            }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun logout(
        userId: String,
        callback: (UserModel?) -> Unit
    ) {
        auth.signOut()
        callback(null) // or handle it as logout success
    }

    override fun updatePassword(
        userId: String,
        oldPassword: String,
        callback: (Boolean, String) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null || user.email == null) {
            callback(false, "User not logged in")
            return
        }

        // Re-authenticate user before password change
        val credential = com.google.firebase.auth.EmailAuthProvider
            .getCredential(user.email!!, oldPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    // For demo: new password is hardcoded. Replace in production
                    val newPassword = "new_secure_password"

                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                callback(true, "Password updated successfully")
                            } else {
                                callback(false, updateTask.exception?.message ?: "Update failed")
                            }
                        }
                } else {
                    callback(false, authTask.exception?.message ?: "Re-authentication failed")
                }
            }
    }
}
