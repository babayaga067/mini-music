package com.example.sangeet.repository

import com.example.sangeet.model.MusicModel
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicRepositoryImpl(
    private val client: Client,
    private val databaseId: String,
    private val collectionId: String
) : MusicRepository {

    private val account = Account(client)
    private val databases = Databases(client)

    private suspend fun getCurrentUserId(): String? {
        return try {
            account.get().id
        } catch (e: AppwriteException) {
            null
        }
    }

    override fun addMusic(music: MusicModel, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val userId = getCurrentUserId()
            if (userId == null) {
                callback(false, "User not authenticated")
                return@launch
            }

            try {
                databases.createDocument(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    documentId = ID.unique(),
                    data = mapOf(
                        "musicName" to music.musicName,
                        "description" to music.description,
                        "imageUrl" to music.imageUrl,
                        "userId" to userId
                    ),
                    permissions = listOf(
                        Permission.read(Role.user(userId)),
                        Permission.write(Role.user(userId))
                    )
                )
                callback(true, "Music added successfully")
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Failed to add music")
            }
        }
    }

    override fun updateMusic(
        musicId: String,
        updatedData: Map<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                databases.updateDocument(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    documentId = musicId,
                    data = updatedData
                )
                callback(true, "Music updated successfully")
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Failed to update music")
            }
        }
    }

    override fun deleteMusic(musicId: String, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                databases.deleteDocument(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    documentId = musicId
                )
                callback(true, "Music deleted successfully")
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Failed to delete music")
            }
        }
    }

    override fun getAllMusics(callback: (Boolean, String, List<MusicModel>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val userId = getCurrentUserId()
            if (userId == null) {
                callback(false, "User not authenticated", null)
                return@launch
            }

            try {
                val result = databases.listDocuments(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    queries = listOf(Query.equal("userId", userId))
                )

                val musics = result.documents.map { doc ->
                    MusicModel(
                        musicId = doc.id,
                        musicName = doc.data["musicName"] as String,
                        description = doc.data["description"] as String,
                        imageUrl = doc.data["imageUrl"] as String,
                        userId = doc.data["userId"] as String
                    )
                }

                callback(true, "Success", musics)
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Failed to fetch musics", null)
            }
        }
    }

    override fun getMusicById(
        musicId: String,
        callback: (Boolean, String, MusicModel?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val doc = databases.getDocument(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    documentId = musicId
                )

                val music = MusicModel(
                    musicId = doc.id,
                    musicName = doc.data["musicName"] as String,
                    description = doc.data["description"] as String,
                    imageUrl = doc.data["imageUrl"] as String,
                    userId = doc.data["userId"] as String
                )

                callback(true, "Success", music)
            } catch (e: AppwriteException) {
                callback(false, e.message ?: "Failed to fetch music", null)
            }
        }
    }
}