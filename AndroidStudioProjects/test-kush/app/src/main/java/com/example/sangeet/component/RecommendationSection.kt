package com.example.sangeet.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sangeet.model.MusicModel
import androidx.compose.foundation.lazy.items

@Composable
fun RecommendationSection(
    musics: List<MusicModel>,
    isLoading: Boolean,
    userId: String,
    favoriteMusics: List<MusicModel>,
    onToggleFavorite: (String) -> Unit,
    onAddToPlaylist: (MusicModel) -> Unit
) {
    Text(
        text = "Recommendation",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(12.dp))

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFE91E63))
            }
        }

        musics.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No music available", color = Color.White.copy(0.7f), fontSize = 14.sp)
            }
        }

        else -> {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(musics) { music ->
                    RecommendationItem(
                        music = music,
                        isFavorite = favoriteMusics.any { it.musicId == music.musicId },
                        onToggleFavorite = { onToggleFavorite(music.musicId) },
                        onAddToPlaylist = { onAddToPlaylist(music) }
                    )
                }
            }
        }
    }
}