package com.example.sangeet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sangeet.model.MusicModel
import com.example.sangeet.repository.MusicRepository


class MusicViewModel(private val repo: MusicRepository) : ViewModel() {

    // LiveData for all musics added by the current user
    private val _userMusics = MutableLiveData<List<MusicModel?>>()
    val userMusics: LiveData<List<MusicModel?>> get() = _userMusics

    // LiveData for a single selected music
    private val _music = MutableLiveData<MusicModel?>()
    val music: LiveData<MusicModel?> get() = _music

    // Loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Error message
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Add a new music
    fun addMusic(music: MusicModel, callback: (Boolean, String) -> Unit) {
        _isLoading.value = true
        repo.addMusic(music) { success, message ->
            _isLoading.value = false
            if (success) {
                getAllMusics()
            } else {
                _errorMessage.value = message
            }
            callback(success, message)
        }
    }

    // Update music by ID
    fun updateMusic(
        musicId: String,
        updatedData: Map<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        _isLoading.value = true
        repo.updateMusic(musicId, updatedData) { success, message ->
            _isLoading.value = false
            if (success) {
                getAllMusics()
                getMusicById(musicId)
            } else {
                _errorMessage.value = message
            }
            callback(success, message)
        }
    }

    // Delete music by ID
    fun deleteMusic(musicId: String, callback: (Boolean, String) -> Unit) {
        _isLoading.value = true
        repo.deleteMusic(musicId) { success, message ->
            _isLoading.value = false
            if (success) {
                getAllMusics()
                _music.value = null
            } else {
                _errorMessage.value = message
            }
            callback(success, message)
        }
    }

    // Fetch all musics for the current user
    fun getAllMusics(callback: ((Boolean, String, List<MusicModel>?) -> Unit)? = null) {
        _isLoading.value = true
        repo.getAllMusics { success, message, musics ->
            _isLoading.value = false
            if (success) {
                _userMusics.postValue(musics ?: emptyList())
            } else {
                _userMusics.postValue(emptyList())
                _errorMessage.value = message
            }
            callback?.invoke(success, message, musics)
        }
    }

    // Fetch a single music by ID
    fun getMusicById(
        musicId: String,
        callback: ((Boolean, String, MusicModel?) -> Unit)? = null
    ) {
        _isLoading.value = true
        repo.getMusicById(musicId) { success, message, music ->
            _isLoading.value = false
            if (success) {
                _music.postValue(music)
            } else {
                _music.postValue(null)
                _errorMessage.value = message
            }
            callback?.invoke(success, message, music)
        }
    }

    // Clear selected music
    fun clearMusicData() {
        _music.value = null
    }

    // Clear all musics
    fun clearAllMusicsData() {
        _userMusics.value = emptyList()
    }

    // Clear error message
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Refresh all data
    fun refreshData() {
        getAllMusics()
    }

    // Check if music list is empty
    fun isMusicsEmpty(): Boolean {
        return _userMusics.value?.isEmpty() ?: true
    }

    // Get count of musics
    fun getMusicsCount(): Int {
        return _userMusics.value?.size ?: 0
    }
}