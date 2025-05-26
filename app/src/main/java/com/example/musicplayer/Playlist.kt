package com.example.musicplayer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PlaylistScreen() {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF240046), Color(0xFF5A189A))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBarSection()
            Spacer(modifier = Modifier.height(20.dp))
            PlaylistsSection()
            Spacer(modifier = Modifier.height(20.dp))
            LikedSongsSection()
            Spacer(modifier = Modifier.height(80.dp))
        }

        BottomNavigationBar()
    }
}

@Composable
fun TopAppBarSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Hi Eva,", color = Color.White, fontSize = 20.sp)
            Text("Good Afternoon", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text("E", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PlaylistsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Playlists", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
    }

    Spacer(modifier = Modifier.height(12.dp))

    val playlists = listOf(
        Pair("Clear Mind", "https://i.imgur.com/1Yc9yOE.png"),
        Pair("Sound of Nature", "https://i.imgur.com/2Kyj3cF.png"),
        Pair("Relax Songs", "https://i.imgur.com/X9tD1Ad.png")
    )

    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        playlists.forEach { (title, url) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .width(100.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(title, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun LikedSongsSection() {
    Text("Liked Songs", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(12.dp))

    val songs = listOf(
        Triple("WILDFLOWER", "Billie Eilish", "https://i.imgur.com/0Z8Z1KZ.png"),
        Triple("Farkanna Hola", "John Chaming Rai", "https://i.imgur.com/YBBjDna.png"),
        Triple("Ghost", "Justin Bieber", "https://i.imgur.com/oe8mUML.png"),
        Triple("Badal Sari", "Swar x John Rai", "https://i.imgur.com/dCm8Exx.png"),
        Triple("Dhairya", "Sajjan Raj Vaidya", "https://i.imgur.com/h3jHv9I.png")
    )

    Column {
        songs.forEach {
            RecentlyPlayedItem(title = it.first, artist = it.second, imageUrl = it.third)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun RecentlyPlayedItem(title: String, artist: String, imageUrl: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = title,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Medium)
            Text(artist, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
        }

        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun BottomNavigationBar1() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        NavigationBar(containerColor = Color(0xFF3C096C)) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text("Home") },
                selected = false,
                onClick = {}
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Search, contentDescription = null) },
                label = { Text("Search") },
                selected = false,
                onClick = {}
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.LibraryMusic, contentDescription = null) },
                label = { Text("Your Library") },
                selected = true,
                onClick = {}
            )
        }
    }
}
