package com.example.sangeet.model

// Represents the many-to-many relationship between User and Music for favorites
data class FavoriteModel(
    val favoriteId: String = "",
    val userId: String = "",
    val musicId: String = "",
    val addedAt: Long = System.currentTimeMillis(),
    
    // Additional metadata for the favorite relationship
    val playCount: Int = 0,
    val lastPlayedAt: Long = 0,
    val rating: Float = 0f, // User's rating for this music (0-5 stars)
    val notes: String = "" // Personal notes about this favorite
)

// Represents the many-to-many relationship between User and Artist for following
data class UserFollowArtistModel(
    val followId: String = "",
    val userId: String = "",
    val artistId: String = "",
    val followedAt: Long = System.currentTimeMillis(),
    val notificationsEnabled: Boolean = true
)