package com.example.sangeet.utils

import android.content.Context
import android.widget.Toast
import com.example.sangeet.viewmodel.FavoriteViewModel

fun toggleFavorite(
    userId: String,
    musicId: String,
    favoriteViewModel: FavoriteViewModel,
    context: Context
) {
    if (userId == "user123") {
        Toast.makeText(context, "Please log in to use favorites", Toast.LENGTH_SHORT).show()
        return
    }

    val currentFavorites = favoriteViewModel.favoriteMusics.value.orEmpty()
    val isFavorite = currentFavorites.any { it.musicId == musicId }

    val callback = { success: Boolean, message: String? ->
        Toast.makeText(context, message ?: if (isFavorite) "Removed from favorites" else "Added to favorites", Toast.LENGTH_SHORT).show()
        if (success) {
            favoriteViewModel.getUserFavoriteMusics(userId)
        }
    }

    if (isFavorite) {
        favoriteViewModel.removeFromFavorites(userId, musicId, callback)
    } else {
        favoriteViewModel.addToFavorites(userId, musicId, callback)
    }
}