package com.example.sangeet.view

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sangeet.component.BottomNavigationBar
import com.example.sangeet.viewmodel.SongViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayingNowScreen(
    navController: NavController,
    viewModel: SongViewModel
) {
    val context = LocalContext.current
    val song by viewModel.song
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: ""

    val mediaPlayer = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    // Load and play song when available
    LaunchedEffect(song) {
        song?.let { songData ->
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songData.audioUrl)
                mediaPlayer.prepare()
                mediaPlayer.start()
                isPlaying = true
            } catch (e: Exception) {
                Log.e("MediaPlayer", "Error loading audio", e)
            }
        }
    }

    // Release MediaPlayer when screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    // Update progress while playing
    LaunchedEffect(isPlaying) {
        while (isPlaying && mediaPlayer.isPlaying) {
            progress = mediaPlayer.currentPosition / mediaPlayer.duration.toFloat()
            delay(500)
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4C005F), Color(0xFF9D00B7))
    )

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Playing now", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Album Art
                if (song?.imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(song!!.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Album Art",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Song Info
                Text(song?.title ?: "Loading...", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(song?.artist ?: "", color = Color.LightGray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Bar
                Slider(
                    value = progress,
                    onValueChange = {
                        progress = it
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.seekTo((it * mediaPlayer.duration).toInt())
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.LightGray,
                        inactiveTrackColor = Color.Gray
                    )
                )

                // Time Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        formatTime(mediaPlayer.currentPosition),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Text(
                        formatTime(mediaPlayer.duration),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = Color.White)
                    Text("-10s", color = Color.White)
                    IconButton(onClick = {
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.pause()
                            isPlaying = false
                        } else {
                            mediaPlayer.start()
                            isPlaying = true
                        }
                    }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Text("+10s", color = Color.White)
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.4f))
            }
        }
    }
}

fun formatTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}