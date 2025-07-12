package com.example.sangeet.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sangeet.model.MusicModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.lazy.items

@Composable
fun RecentlyPlayedSection(
    musics: List<MusicModel>,
    isLoading: Boolean,
    userId: String,
    favoriteMusics: List<MusicModel>,
    onToggleFavorite: (String) -> Unit,
    onAddToPlaylist: (MusicModel) -> Unit
) {
    Text(
        text = "Recently Played",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )

    Spacer(modifier = Modifier.height(12.dp))

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFE91E63))
            }
        }

        musics.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No music uploaded yet", color = Color.White.copy(0.7f), fontSize = 14.sp)
            }
        }

        else -> {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(musics) { music ->
                    MusicCard(
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
