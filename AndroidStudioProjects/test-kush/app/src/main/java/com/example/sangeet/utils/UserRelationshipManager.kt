package com.example.sangeet.utils

import com.example.sangeet.model.*
import com.example.sangeet.repository.*

sealed class RelationshipResult {
    data class Success(val message: String, val data: Any? = null) : RelationshipResult()
    data class Failure(val message: String) : RelationshipResult()
}

class UserRelationshipManager(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val playlistRepository: PlaylistRepository,
    private val artistRepository: ArtistRepository,
    private val musicRepository: MusicRepository
) {

    fun addMusicToFavorites(userId: String, musicId: String, callback: (RelationshipResult) -> Unit) {
        val favoriteId = generateFavoriteId()
        val favoriteModel = FavoriteModel(
            favoriteId = favoriteId,
            userId = userId,
            musicId = musicId,
            addedAt = System.currentTimeMillis()
        )

        favoriteRepository.addToFavorites(userId, musicId) { success, msg ->
            if (!success) return@addToFavorites callback(RelationshipResult.Failure("Favorite creation failed: $msg"))

            userRepository.addFavoriteToUser(userId, favoriteId) { userSuccess, userMsg ->
                if (userSuccess) {
                    callback(RelationshipResult.Success("Music added to favorites"))
                } else {
                    favoriteRepository.removeFromFavorites(userId, musicId) { _, _ -> }
                    callback(RelationshipResult.Failure("User update failed: $userMsg"))
                }
            }
        }
    }

    fun followArtist(userId: String, artistId: String, callback: (RelationshipResult) -> Unit) {
        userRepository.addFollowedArtistToUser(userId, artistId) { userSuccess, userMsg ->
            if (!userSuccess) return@addFollowedArtistToUser callback(RelationshipResult.Failure("User follow failed: $userMsg"))

            artistRepository.followArtist(artistId) { artistSuccess, artistMsg ->
                if (artistSuccess) {
                    callback(RelationshipResult.Success("Followed artist successfully"))
                } else {
                    userRepository.removeFollowedArtistFromUser(userId, artistId) { _, _ -> }
                    callback(RelationshipResult.Failure("Artist update failed: $artistMsg"))
                }
            }
        }
    }

    fun createPlaylistWithMusic(
        userId: String,
        playlistName: String,
        description: String,
        musicIds: List<String>,
        callback: (RelationshipResult) -> Unit
    ) {
        val playlistId = generatePlaylistId()
        val playlistModel = PlaylistModel(
            playlistId = playlistId,
            userId = userId,
            playlistName = playlistName,
            description = description,
            musicIds = musicIds,
            createdAt = System.currentTimeMillis()
        )

        playlistRepository.createPlaylist(playlistModel) { playlistSuccess, playlistMsg ->
            if (!playlistSuccess) return@createPlaylist callback(RelationshipResult.Failure("Playlist creation failed: $playlistMsg"))

            userRepository.addPlaylistToUser(userId, playlistId) { userSuccess, userMsg ->
                if (userSuccess) {
                    callback(RelationshipResult.Success("Playlist created successfully", playlistId))
                } else {
                    playlistRepository.deletePlaylist(playlistId) { _, _ -> }
                    callback(RelationshipResult.Failure("User playlist update failed: $userMsg"))
                }
            }
        }
    }

    fun uploadMusicAsUser(
        userId: String,
        musicModel: MusicModel,
        callback: (RelationshipResult) -> Unit
    ) {
        val updatedMusicModel = musicModel.copy(
            uploadedBy = userId,
            uploadedAt = System.currentTimeMillis()
        )

        musicRepository.addMusic(updatedMusicModel) { musicSuccess, musicMsg ->
            if (!musicSuccess) return@addMusic callback(RelationshipResult.Failure("Music upload failed: $musicMsg"))

            userRepository.addUploadedMusicToUser(userId, updatedMusicModel.musicId) { userSuccess, userMsg ->
                if (userSuccess) {
                    userRepository.incrementUserUploadCount(userId) { _, _ -> }
                    callback(RelationshipResult.Success("Music uploaded", updatedMusicModel.musicId))
                } else {
                    musicRepository.deleteMusic(updatedMusicModel.musicId) { _, _ -> }
                    callback(RelationshipResult.Failure("User upload update failed: $userMsg"))
                }
            }
        }
    }

    fun getUserWithRelationships(userId: String, callback: (UserProfileWithRelationships?) -> Unit) {
        userRepository.getUserById(userId) { success, _, user ->
            if (!success || user == null) return@getUserById callback(null)

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
    }

    // ID generators
    private fun generateFavoriteId(): String = "fav_${System.currentTimeMillis()}_${(1000..9999).random()}"
    private fun generatePlaylistId(): String = "playlist_${System.currentTimeMillis()}_${(1000..9999).random()}"
}