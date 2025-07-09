package com.example.sangeet.repository

import com.example.sangeet.model.MusicModel

interface MusicRepository {
    fun addMusic(music: MusicModel, callback: (Boolean, String) -> Unit)
    fun updateMusic(musicId: String, updatedData: Map<String, Any?>, callback: (Boolean, String) -> Unit)
    fun deleteMusic(musicId: String, callback: (Boolean, String) -> Unit)
    fun getAllMusics(callback: (Boolean, String, List<MusicModel>?) -> Unit)
    fun getMusicById(musicId: String, callback: (Boolean, String, MusicModel?) -> Unit)
}