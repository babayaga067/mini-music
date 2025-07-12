package com.example.sangeet.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sangeet.model.MusicModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width



@Composable
fun AddToPlaylistDialog(
    music: MusicModel,
    playlists: List<com.example.sangeet.model.PlaylistModel>,
    onDismiss: () -> Unit,
    onCreateNew: () -> Unit,
    onAddToPlaylist: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add to Playlist", color = Color.White, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(
                    "Select a playlist to add \"${music.musicName}\":",
                    color = Color.White.copy(0.8f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (playlists.isEmpty()) {
                    Text("No playlists found. Create a playlist first.", color = Color.White.copy(0.6f), fontSize = 12.sp)
                } else {
                    LazyColumn(modifier = Modifier.height(200.dp)) {
                        items(playlists) { playlist ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        onAddToPlaylist(playlist.playlistId)
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.PlaylistPlay, contentDescription = null, tint = Color(0xFFE91E63), modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(playlist.playlistName, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                        if (playlist.description.isNotEmpty()) {
                                            Text(playlist.description, color = Color.White.copy(0.7f), fontSize = 12.sp, maxLines = 1)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onCreateNew) {
                Text("Create New", color = Color(0xFFE91E63))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White.copy(0.7f))
            }
        },
        containerColor = Color(0xFF1A1A2E),
        textContentColor = Color.White
    )
}