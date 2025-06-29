package com.example.sangeet.repository

import com.example.sangeet.model.UserModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class UserRepositoryImpl(instance: FirebaseAuth, instance1: FirebaseFirestore) : UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful, task.exception?.message ?: "Login successful")
            }
    }

    override fun register(
        userId: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        gender: String,
        address: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid.orEmpty()
                    callback(true, "Registration successful", uid)
                } else {
                    callback(false, task.exception?.message ?: "Registration failed", "")
                }
            }
    }

    override fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        ref.child(userId).setValue(userModel).addOnCompleteListener { task ->
            callback(task.isSuccessful, task.exception?.message ?: "User added")
        }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            callback(task.isSuccessful, task.exception?.message ?: "Reset link sent")
        }
    }

    override fun getUserById(userId: String, callback: (UserModel?) -> Unit) {
        ref.child(userId).get().addOnSuccessListener { snapshot ->
            callback(snapshot.getValue(UserModel::class.java))
        }.addOnFailureListener {
            callback(null)
        }
    }

    override fun updateProfile(userId: String, updates: Map<String, Any?>, callback: (Boolean, String) -> Unit) {
        ref.child(userId).updateChildren(updates).addOnCompleteListener { task ->
            callback(task.isSuccessful, task.exception?.message ?: "Profile updated")
        }
    }

    override fun updatePassword(
        userId: String,
        oldPassword: String,
        newPassword: String,
        callback: (Boolean, String) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null || user.email == null) {
            callback(false, "User not logged in")
            return
        }

        val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
        user.reauthenticate(credential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                    callback(updateTask.isSuccessful, updateTask.exception?.message ?: "Password updated")
                }
            } else {
                callback(false, authTask.exception?.message ?: "Re-authentication failed")
            }
        }
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun logout(callback: () -> Unit) {
        auth.signOut()
        callback()
    }
}
