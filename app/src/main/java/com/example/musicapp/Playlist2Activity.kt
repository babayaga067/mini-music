package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Playlist2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Playlist2Body()
        }
    }
}

@Composable
fun Playlist2Body() {

    val playlists = listOf(
        Playlist("Relax Songs", R.drawable.img1),
        Playlist("Driving vocals", R.drawable.img2),
        Playlist("Add new playlist", R.drawable.img3, isAddNew = true)
    )

    val favouriteSongs = remember {
        mutableStateListOf(
            Song("WILDFLOWER", "Billie Eilish", R.drawable.wildflower,true),
            Song("Farkanna Hola", "John Chaming Rai", R.drawable.farkanna,true),
            Song("Ghost", "Justin Bieber", R.drawable.ghost,true),
            Song("Badal Sari", "Swar x John Rai", R.drawable.badal,true),
            Song("Dhairya", "Reeze ft. Vekari", R.drawable.dhairya,true)
        )
    }
    Image(
        painterResource(R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hi Eva,", color = Color.White, fontSize = 18.sp)
                    Text("Good Afternoon", color = Color.White, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(8.dp)
                        .size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Playlists Section
            Text("Playlists", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow {
                items(playlists) { playlist ->
                    PlaylistCard(playlist)
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Favourite Section
            Text("Favourite", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))

            favouriteSongs.forEach { song ->
                SongItem(song = song) {
                    song.isFavorite = !song.isFavorite
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Navigation
            BottomNav()
        }
    }
}

data class Playlist(val name: String, val imageRes: Int, val isAddNew: Boolean = false)

@Composable
fun PlaylistCard(playlist: Playlist) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .clickable { }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = playlist.imageRes),
            contentDescription = playlist.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            playlist.name,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun BottomNav() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Icon(imageVector = Icons.Default.Home, contentDescription = "Home", tint = Color.White)
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.White)
        Icon(imageVector = Icons.Default.LibraryMusic, contentDescription = "Library", tint = Color.White)
    }

}
@Preview(showBackground = true)
@Composable
fun PreviewMyScreen() {
    Playlist2Body()
}