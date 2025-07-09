package com.example.sangeet.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sangeet.model.SongModel
import io.appwrite.Client
import io.appwrite.services.Databases
import kotlinx.coroutines.launch

class SongViewModel(client: Client) : ViewModel() {
    private val database = Databases(client)
    private val _song = mutableStateOf<SongModel?>(null)
    val song = _song

    fun fetchSong(documentId: String) {
        viewModelScope.launch {
            try {
                val doc = database.getDocument(
                    databaseId = "your-database-id",
                    collectionId = "your-collection-id",
                    documentId = documentId
                )
                _song.value = SongModel(
                    title = doc.data["title"] as String,
                    artist = doc.data["artist"] as String,
                    imageUrl = doc.data["imageUrl"] as String,
                    audioUrl = doc.data["audioUrl"] as String
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}