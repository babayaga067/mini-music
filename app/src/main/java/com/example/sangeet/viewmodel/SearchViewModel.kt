package com.example.sangeet.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sangeet.R

data class Song(
    val title: String,
    val artist: String,
    val imageRes: Int,
    var isFavorite: Boolean = false
)

class SearchViewModel : ViewModel() {
    var searchQuery = mutableStateOf("")
        private set

    private val _songs = mutableStateListOf(
        Song("Blue", "Yung Kai", R.drawable.blue),
        Song("Dandelions", "Ruth B", R.drawable.dandelions),
        Song("Upahaar", "Swopna Suman", R.drawable.upahar, true),
        Song("Jhim Jhumaune Aankha", "Ekdev Limbu", R.drawable.jhim),
        Song("Apna Bana Le", "Arijit Singh", R.drawable.apna),
        Song("Furfuri", "Unknown", R.drawable.furfuri)
    )
    val songs: List<Song> get() = _songs

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun toggleFavorite(song: Song) {
        val index = _songs.indexOfFirst { it.title == song.title && it.artist == song.artist }
        if (index != -1) {
            _songs[index] = _songs[index].copy(isFavorite = !_songs[index].isFavorite)
        }
    }

    fun filteredSongs(): List<Song> {
        val query = searchQuery.value.lowercase()
        return _songs.filter {
            it.title.lowercase().contains(query) || it.artist.lowercase().contains(query)
        }
    }
}