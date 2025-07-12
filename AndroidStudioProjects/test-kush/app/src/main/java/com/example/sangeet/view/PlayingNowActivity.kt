package com.example.sangeet.view

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.sangeet.viewmodel.MusicViewModel
import com.example.sangeet.repository.MusicRepositoryImpl

class PlayingNowActivity : ComponentActivity() {
    private val musicViewModel by lazy { MusicViewModel(MusicRepositoryImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val musicId = intent.getStringExtra("musicId") ?: ""
        setContent {
            val navController = rememberNavController()
            PlayingNowScreen(
                musicId = musicId,
                musicViewModel = musicViewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun PlayingNowScreen(
    musicId: String,
    musicViewModel: MusicViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val musicLiveData by musicViewModel.music.observeAsState()

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var durationText by remember { mutableStateOf("0:00") }
    var currentTimeText by remember { mutableStateOf("0:00") }

    // Load music info
    LaunchedEffect(musicId) {
        if (musicId.isNotEmpty()) {
            musicViewModel.getMusicById(musicId)
        }
    }

    // Setup MediaPlayer safely
    LaunchedEffect(musicLiveData) {
        mediaPlayer?.release()
        mediaPlayer = null

        val audioUrl = musicLiveData?.audioUrl.orEmpty()
        if (audioUrl.isNotEmpty()) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioUrl)
                prepareAsync()
                setOnPreparedListener {
                    start()
                    isPlaying = true
                    durationText = formatTime(duration)
                }
                setOnCompletionListener {
                    isPlaying = false
                    progress = 0f
                }
            }
        }
    }

    // Track playback progress
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            mediaPlayer?.let {
                val current = it.currentPosition
                val total = it.duration
                if (total > 0) {
                    progress = current / total.toFloat()
                    currentTimeText = formatTime(current)
                }
            }
            kotlinx.coroutines.delay(500)
        }
    }

    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    val gradient = Brush.verticalGradient(listOf(Color(0xFF4C005F), Color(0xFF9D00B7)))

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4C005F), contentColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") }, selected = false, onClick = { navController.navigate("dashboard") })
                NavigationBarItem(icon = { Icon(Icons.Default.Search, null) }, label = { Text("Search") }, selected = false, onClick = { navController.navigate("search") })
                NavigationBarItem(icon = { Icon(Icons.Default.LibraryMusic, null) }, label = { Text("Library") }, selected = true, onClick = { navController.navigate("library") })
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ) {
            musicLiveData?.let { music ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Playing now", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    AsyncImage(
                        model = music.imageUrl.ifEmpty { "https://via.placeholder.com/220" },
                        contentDescription = music.musicName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(music.musicName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(music.artistName, color = Color.LightGray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Slider(
                        value = progress,
                        onValueChange = {
                            progress = it
                            mediaPlayer?.seekTo((it * (mediaPlayer?.duration ?: 0)).toInt())
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.LightGray,
                            inactiveTrackColor = Color.Gray
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(currentTimeText, color = Color.White, fontSize = 12.sp)
                        Text(durationText, color = Color.White, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = Color.White)
                        Text("-10s", color = Color.White)
                        IconButton(onClick = {
                            mediaPlayer?.let {
                                if (it.isPlaying) {
                                    it.pause()
                                    isPlaying = false
                                } else {
                                    it.start()
                                    isPlaying = true
                                }
                            }
                        }) {
                            Icon(
                                if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color.White,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        Text("+10s", color = Color.White)
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

fun formatTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}