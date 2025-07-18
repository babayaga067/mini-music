package com.example.musicplayer.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.musicplayer.ui.theme.MusicplayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicplayerTheme {
                PlayingNowScreen() // Launch Playing Now screen on app start
            }
        }
    }
}