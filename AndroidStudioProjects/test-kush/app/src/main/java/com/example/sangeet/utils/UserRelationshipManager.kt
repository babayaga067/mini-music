package com.example.sangeet.utils

import com.example.sangeet.model.*
import com.example.sangeet.repository.*

/**
 * Utility class to manage complex user relationships and operations
 * Demonstrates how the enhanced UserModel relationships work in practice
 */
class UserRelationshipManager(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val playlistRepository: PlaylistRepository,
    private val artistRepository: ArtistRepository,
    private val musicRepository: MusicRepository
) {

    /**
     * Complete workflow: User adds music to favorites
     * This demonstrates the relationship between User, Music, and Favorites
     */
    fun addMusicToUserFavorites(
        userId: String,
        musicId: String,
        callback: (Boolean, String) -> Unit
    ) {
        // Step 1: Create favorite relationship
        val favoriteModel = FavoriteModel(
            favoriteId = generateFavoriteId(),
            userId = userId,
            musicId = musicId,
            addedAt = System.currentTimeMillis()
        )

        // Step 2: Add to favorites collection
        favoriteRepository.addToFavorites(userId, musicId) { success, message ->
            if (success) {
                // Step 3: Update user's favorite list
                userRepository.addFavoriteToUser(userId, favoriteModel.favoriteId) { userSuccess, userMessage ->
                    if (userSuccess) {
                        callback(true, "Music added to favorites successfully")
                    } else {
                        // Rollback: Remove from favorites if user update failed
                        favoriteRepository.removeFromFavorites(userId, musicId) { _, _ -> }
                        callback(false, "Failed to update user favorites: $userMessage")
                    }
                }
            } else {
                callback(false, "Failed to create favorite: $message")
            }
        }
    }

    /**
     * Complete workflow: User follows an artist
     * This demonstrates the many-to-many relationship between User and Artist
     */
    fun followArtist(
        userId: String,
        artistId: String,
        callback: (Boolean, String) -> Unit
    ) {
        // Step 1: Update user's followed artists (simplified implementation)
        // TODO: Create UserFollowArtistModel and repository if needed
        userRepository.addFollowedArtistToUser(userId, artistId) { userSuccess, userMessage ->
            if (userSuccess) {
                // Step 4: Update artist's followers
                artistRepository.followArtist(artistId) { artistSuccess, artistMessage ->
                    if (artistSuccess) {
                        callback(true, "Successfully followed artist")
                    } else {
                        // Rollback: Remove from user's followed list
                        userRepository.removeFollowedArtistFromUser(userId, artistId) { _, _ -> }
                        callback(false, "Failed to update artist followers: $artistMessage")
                    }
                }
            } else {
                callback(false, "Failed to update user following: $userMessage")
            }
        }
    }

    /**
     * Complete workflow: User creates a playlist and adds music
     * This demonstrates User -> Playlist -> Music relationships
     */
    fun createPlaylistWithMusic(
        userId: String,
        playlistName: String,
        description: String,
        musicIds: List<String>,
        callback: (Boolean, String, String?) -> Unit // success, message, playlistId
    ) {
        // Step 1: Create playlist
        val playlistModel = PlaylistModel(
            playlistId = generatePlaylistId(),
            userId = userId,
            playlistName = playlistName,
            description = description,
            musicIds = musicIds,
            createdAt = System.currentTimeMillis()
        )

        // Step 2: Add playlist to database
        playlistRepository.createPlaylist(playlistModel) { success, message ->
            if (success) {
                // Step 3: Update user's playlist list
                userRepository.addPlaylistToUser(userId, playlistModel.playlistId) { userSuccess, userMessage ->
                    if (userSuccess) {
                        callback(true, "Playlist created successfully", playlistModel.playlistId)
                    } else {
                        // Rollback: Delete playlist if user update failed
                        playlistRepository.deletePlaylist(playlistModel.playlistId) { _, _ -> }
                        callback(false, "Failed to update user playlists: $userMessage", null)
                    }
                }
            } else {
                callback(false, "Failed to create playlist: $message", null)
            }
        }
    }

    /**
     * Complete workflow: User uploads music
     * This demonstrates User -> Music -> Artist relationships
     */
    fun uploadMusicAsUser(
        userId: String,
        musicModel: MusicModel,
        callback: (Boolean, String, String?) -> Unit // success, message, musicId
    ) {
        // Ensure the music is linked to the user
        val updatedMusicModel = musicModel.copy(
            uploadedBy = userId,
            uploadedAt = System.currentTimeMillis()
        )

        // Step 1: Add music to database
        musicRepository.addMusic(updatedMusicModel) { success, message ->
            if (success) {
                // Step 2: Update user's uploaded music list
                userRepository.addUploadedMusicToUser(userId, updatedMusicModel.musicId) { userSuccess, userMessage ->
                    if (userSuccess) {
                        // Step 3: Update user's upload count
                        userRepository.incrementUserUploadCount(userId) { _, _ -> }
                        callback(true, "Music uploaded successfully", updatedMusicModel.musicId)
                    } else {
                        // Rollback: Remove music if user update failed
                        musicRepository.deleteMusic(updatedMusicModel.musicId) { _, _ -> }
                        callback(false, "Failed to update user uploads: $userMessage", null)
                    }
                }
            } else {
                callback(false, "Failed to upload music: $message", null)
            }
        }
    }

    /**
     * Get comprehensive user profile with all relationships
     */
    fun getUserWithRelationships(
        userId: String,
        callback: (UserProfileWithRelationships?) -> Unit
    ) {
        userRepository.getUserById(userId) { success, _, user ->
            if (success && user != null) {
                favoriteRepository.getUserFavorites(userId) { success, _, favorites ->
                    playlistRepository.getUserPlaylists(userId) { playlistSuccess, _, playlists ->

                        val profile = UserProfileWithRelationships(
                            user = user,
                            favoriteMusic = emptyList(),
                            playlists = playlists ?: emptyList(),
                            uploadedMusic = emptyList(),
                            followedArtists = emptyList()
                        )

                        callback(profile)
                    }
                }
            } else {
                callback(null)
            }
        }
    }

    // Helper functions to generate IDs
    private fun generateFavoriteId(): String = "fav_${System.currentTimeMillis()}_${(1000..9999).random()}"
    private fun generateFollowId(): String = "follow_${System.currentTimeMillis()}_${(1000..9999).random()}"
    private fun generatePlaylistId(): String = "playlist_${System.currentTimeMillis()}_${(1000..9999).random()}"
}

/**
 * Data class representing a complete user profile with all relationships
 */
data class UserProfileWithRelationships(
    val user: UserModel,
    val favoriteMusic: List<MusicModel>,
    val playlists: List<PlaylistModel>,
    val uploadedMusic: List<MusicModel>,
    val followedArtists: List<ArtistModel>
)

/**
 * Extension functions for easier relationship management
 */
fun UserModel.hasFavorite(musicId: String): Boolean {
    return favoriteIds.any { it.contains(musicId) }
}

fun UserModel.isFollowing(artistId: String): Boolean {
    return followedArtistIds.contains(artistId)
}

fun UserModel.ownsPlaylist(playlistId: String): Boolean {
    return playlistIds.contains(playlistId)
}

fun UserModel.hasUploadedMusic(musicId: String): Boolean {
    return uploadedMusicIds.contains(musicId)
}