package com.example.sangeet.model


data class MusicModel(
    val musicId : String = "",
    val  musicName: String = "",
    val description : String = "",
    val imageUrl: String ="",
    val userId: String = "", //Reference only
)