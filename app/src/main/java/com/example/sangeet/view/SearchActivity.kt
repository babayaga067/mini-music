package com.example.sangeet.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sangeet.R // Import your R file to access drawables

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SearchScreen()
        }
    }
}

@Composable
fun SearchScreen() {
    val gradientColors = listOf(Color(0xFF6100FF), Color(0xFF9A00FF))
    // Initialize searchQuery with an empty string
    var searchQuery by remember { mutableStateOf("") }

    // Use mutableStateListOf for dynamic lists if you plan to add/remove songs
    // For a fixed list, mutableStateOf(listOf(...)) is fine, but mutableStateListOf allows direct modifications.
    val songs = remember {
        mutableStateOf(
            listOf(
//                Song("Easy On Me", "Adelle", R.drawable.easy_on_me),
                Song("Blue", "Yung Kai", R.drawable.blue),
                Song("Dandelions", "Ruth B", R.drawable.dandelions),
                Song("Upahaar", "Swopna Suman", R.drawable.upahar, true),
                Song("Jhim Jhumaune Aankha", "Ekdev Limbu", R.drawable.jhim),
                Song("Apna Bana Le", "Arijit Singh", R.drawable.apna),
                Song("Furfuri", "Unknown", R.drawable.furfuri)
            )
        )
    }

    // Filter songs based on search query
    val filteredSongs = songs.value.filter { song ->
        song.title.contains(searchQuery, ignoreCase = true) ||
                song.artist.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
            .padding(16.dp)
    ) {
        Column {
            // Top bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /* Handle back button click */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("Search", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Handle profile icon click */ }) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search here", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                colors = TextFieldDefaults.colors( // Use TextFieldDefaults.colors for Material 3
                    focusedContainerColor = Color.Transparent, // Corrected parameter for Material 3
                    unfocusedContainerColor = Color.Transparent, // Corrected parameter for Material 3
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Songs for you
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Songs For You", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Handle arrow forward click */ }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(filteredSongs) { song ->
                    // Make a copy of the song to ensure recomposition when isFavorite changes
                    // or, ideally, pass a mutable state directly from the data source.
                    val currentSong = song.copy()
                    SongItem(song = currentSong) {
                        // Find the song in the original list and update its favorite status
                        songs.value = songs.value.map {
                            if (it.title == currentSong.title && it.artist == currentSong.artist) {
                                it.copy(isFavorite = !it.isFavorite)
                            } else {
                                it
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Song(
    val title: String,
    val artist: String,
    val imageRes: Int,
    var isFavorite: Boolean = false // This needs to be 'var' if you want to modify it directly
)

@Composable
fun SongItem(song: Song, onHeartClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = song.imageRes),
            contentDescription = song.title,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(song.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(song.artist, color = Color.LightGray, fontSize = 12.sp)
        }

        IconButton(onClick = onHeartClick) {
            Icon(
                imageVector = if (song.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (song.isFavorite) Color.White else Color.LightGray
            )
        }
    }
}