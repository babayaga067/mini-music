package com.example.musicplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlaylistScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3C096C)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome to Playlist Section",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}