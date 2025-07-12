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

    favoriteViewModel.favoriteMusics.value?.let { favorites ->
        val isFavorite = favorites.any { it.musicId == musicId }
        if (isFavorite) {
            favoriteViewModel.removeFromFavorites(userId, musicId) { success, message ->
                Toast.makeText(context, message ?: "Removed from favorites", Toast.LENGTH_SHORT).show()
                if (success) favoriteViewModel.getUserFavoriteMusics(userId)
            }
        } else {
            favoriteViewModel.addToFavorites(userId, musicId) { success, message ->
                Toast.makeText(context, message ?: "Added to favorites", Toast.LENGTH_SHORT).show()
                if (success) favoriteViewModel.getUserFavoriteMusics(userId)
            }
        }
    }
}