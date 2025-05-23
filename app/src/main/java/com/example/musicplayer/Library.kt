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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun LibraryScreen() {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF240046), Color(0xFF5A189A)),
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
            YourLibrarySection()
            Spacer(modifier = Modifier.height(20.dp))
            RecentlyPlayedSection()
            Spacer(modifier = Modifier.height(80.dp)) // space for bottom nav
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
fun YourLibrarySection() {
    Text("Your Library", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

    Spacer(modifier = Modifier.height(12.dp))
    Row {
        LibraryCard("Liked Songs", "29 songs", selected = true)
        Spacer(modifier = Modifier.width(12.dp))
        LibraryCard("Playlists", "4 playlists", selected = false)
    }
}

@Composable
fun LibraryCard(title: String, subtitle: String, selected: Boolean) {
    val background = if (selected) Color.Transparent else Color.White.copy(alpha = 0.1f)
    val border = if (selected) BorderStroke(2.dp, Color.Cyan) else null

    Box(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .background(background, shape = RoundedCornerShape(12.dp))
            .border(border ?: BorderStroke(0.dp, Color.Transparent), shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Icon(
                imageVector = if (title == "Liked Songs") Icons.Default.Favorite else Icons.Default.PlaylistPlay,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

@Composable
fun RecentlyPlayedSection() {
    Text("Recently Played", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(12.dp))

    val songs = listOf(
        Triple("Dandelions", "Ruth B", "https://i.scdn.co/image/ab67616d0000b273cba27aa2d17c25c934db68ea"),
        Triple("Blue", "Yung Kai", "https://i.scdn.co/image/ab67616d00001e02457bdffde0986cc0403cb51d"),
        Triple("Night Changes", "One Direction", "https://i.scdn.co/image/ab67616d0000b2736e991ddf5a6ef2e9a5a5998e"),
        Triple("Dhairya", "Sajjan Raj Vaidya", "https://i.scdn.co/image/ab67616d0000b27305f6aa43a8c93ce2a5a938c0"),
        Triple("Sarara Sarara", "Kehar Sing Limbu", "https://i.scdn.co/image/ab67616d0000b2733c3b3483d2dcf8c3fd0e2d8d"),
        Triple("Furfuri", "Artist", "https://i.scdn.co/image/ab67616d0000b2732dc4aab9f8a6aa292b3cfabe")
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
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
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
fun BottomNavigationBar() {
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

