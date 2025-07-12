package com.example.sangeet.repository

import com.example.sangeet.model.UserModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepositoryImpl : UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful, task.exception?.message ?: "Login successful")
            }
    }

    override fun register(
        email: String,
        password: String,
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

    override fun getUserById(userId: String, callback: (Boolean, String, UserModel?) -> Unit) {
        ref.child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(UserModel::class.java)
                callback(true, "User loaded", user)
            }.addOnFailureListener { error ->
                callback(false, error.message ?: "Failed to load user", null)
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

    // Relationship Management Implementation
    override fun addFavoriteToUser(userId: String, favoriteId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("favoriteIds").get().addOnSuccessListener { snapshot ->
            val currentFavorites = (snapshot.value as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            val updatedFavorites = currentFavorites.toMutableList().apply { add(favoriteId) }
            ref.child(userId).child("favoriteIds").setValue(updatedFavorites)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message ?: "Favorite added")
                }
        }.addOnFailureListener {
            callback(false, it.message ?: "Failed to add favorite")
        }
    }

    override fun removeFavoriteFromUser(userId: String, favoriteId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("favoriteIds").get().addOnSuccessListener { snapshot ->
            val currentFavorites = (snapshot.value as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            val updatedFavorites = currentFavorites.toMutableList().apply { remove(favoriteId) }
            ref.child(userId).child("favoriteIds").setValue(updatedFavorites)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message ?: "Favorite removed")
                }
        }.addOnFailureListener {
            callback(false, it.message ?: "Failed to remove favorite")
        }
    }

    override fun addPlaylistToUser(userId: String, playlistId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("playlistIds").get().addOnSuccessListener { snapshot ->
            val currentPlaylists = (snapshot.value as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            val updatedPlaylists = currentPlaylists.toMutableList().apply { add(playlistId) }
            ref.child(userId).child("playlistIds").setValue(updatedPlaylists)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message ?: "Playlist added")
                }
        }.addOnFailureListener {
            callback(false, it.message ?: "Failed to add playlist")
        }
    }

    override fun removePlaylistFromUser(userId: String, playlistId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("playlistIds").get().addOnSuccessListener { snapshot ->
            val currentPlaylists = (snapshot.value as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            val updatedPlaylists = currentPlaylists.toMutableList().apply { remove(playlistId) }
            ref.child(userId).child("playlistIds").setValue(updatedPlaylists)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message ?: "Playlist removed")
                }
        }.addOnFailureListener {
            callback(false, it.message ?: "Failed to remove playlist")
        }
    }

    override fun addFollowedArtistToUser(userId: String, artistId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("followedArtistIds").get().addOnSuccessListener { snapshot ->
            val currentFollowed = (snapshot.value as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            val updatedFollowed = currentFollowed.toMutableList().apply { add(artistId) }
            ref.child(userId).child("followedArtistIds").setValue(updatedFollowed)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message ?: "Artist followed")
                }
        }.addOnFailureListener {
            callback(false, it.message ?: "Failed to follow artist")
        }
    }

    override fun removeFollowedArtistFromUser(userId: String, artistId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("followedArtistIds").get().addOnSuccessListener { snapshot ->
            val currentFollowed = (snapshot.value as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            val updatedFollowed = currentFollowed.toMutableList().apply { remove(artistId) }
            ref.child(userId).child("followedArtistIds").setValue(updatedFollowed)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message ?: "Artist unfollowed")
                }
        }.addOnFailureListener {
            callback(false, it.message ?: "Failed to unfollow artist")
        }
    }

    override fun addUploadedMusicToUser(userId: String, musicId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("uploadedMusicIds").get().addOnSuccessListener { snapshot ->
            val currentUploads = (snapshot.value as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            val updatedUploads = currentUploads.toMutableList().apply { add(musicId) }
            
            val updates = mapOf(
                "uploadedMusicIds" to updatedUploads,
                "totalUploads" to updatedUploads.size
            )
            
            ref.child(userId).updateChildren(updates)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message ?: "Music upload recorded")
                }
        }.addOnFailureListener {
            callback(false, it.message ?: "Failed to record music upload")
        }
    }

    // User Statistics Implementation
    override fun incrementUserPlayCount(userId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("totalPlaysCount").get().addOnSuccessListener { snapshot ->
            val currentCount = snapshot.getValue(Int::class.java) ?: 0
            ref.child(userId).child("totalPlaysCount").setValue(currentCount + 1)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message ?: "Play count updated")
                }
        }.addOnFailureListener {
            callback(false, it.message ?: "Failed to update play count")
        }
    }

    override fun incrementUserUploadCount(userId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).child("totalUploads").get().addOnSuccessListener { snapshot ->
            val currentCount = snapshot.getValue(Int::class.java) ?: 0
            ref.child(userId).child("totalUploads").setValue(currentCount + 1)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful, task.exception?.message ?: "Upload count updated")
                }
        }.addOnFailureListener {
            callback(false, it.message ?: "Failed to update upload count")
        }
    }

    override fun updateLastLoginTime(userId: String, callback: (Boolean, String) -> Unit) {
        val currentTime = System.currentTimeMillis()
        ref.child(userId).child("lastLoginAt").setValue(currentTime)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful, task.exception?.message ?: "Last login updated")
            }
    }

    // User Search and Discovery Implementation
    override fun searchUsers(query: String, callback: (List<UserModel>) -> Unit) {
        ref.orderByChild("fullName").startAt(query).endAt(query + "\uf8ff")
            .get().addOnSuccessListener { snapshot ->
                val users = mutableListOf<UserModel>()
                snapshot.children.forEach { child ->
                    child.getValue(UserModel::class.java)?.let { users.add(it) }
                }
                callback(users)
            }.addOnFailureListener {
                callback(emptyList())
            }
    }

    override fun getUsersByIds(
        userIds: List<String>, callback: (List<UserModel>) -> Unit) {
        if (userIds.isEmpty()) {
            callback(emptyList())
            return
        }
        
        val users = mutableListOf<UserModel>()
        var completedRequests = 0
        
        userIds.forEach { userId ->
            ref.child(userId).get().addOnSuccessListener { snapshot ->
                snapshot.getValue(UserModel::class.java)?.let { users.add(it) }
                completedRequests++
                if (completedRequests == userIds.size) {
                    callback(users)
                }
            }.addOnFailureListener {
                completedRequests++
                if (completedRequests == userIds.size) {
                    callback(users)
                }
            }
        }
    }
}
