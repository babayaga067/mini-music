package com.example.musicplayer.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.R

// Data classes to represent the UI models
data class Playlist(val name: String, val imageRes: Int)

class PlaylistActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen() {
    // Data mimicking the image content
    val playlists = listOf(
        Playlist("Clear Mind", R.drawable.mayalu),
        Playlist("Sound of Nature", R.drawable.stay),
        Playlist("Relax Songs", R.drawable.lover)
    )

    val favouriteSongs = listOf(
        Song("WILDFLOWER", "Billie Eilish", R.drawable.upahaar),
        Song("Farkanna Hola", "Sabin Rai & The Elektrix", R.drawable.cruel),
        Song("Ghost", "Justin Bieber", R.drawable.stay),
        Song("Badal Sari", "Sabin Rai", R.drawable.mayalu),
        Song("Dhairya", "Sushant KC", R.drawable.lover)
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2A0D4E), Color(0xFF1B0C2B))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Hi Eva,", fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f))
                        Text(text = "Good Afternoon", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle Menu Click */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Yellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "E", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = { BottomNavBar() },
        containerColor = Color.Transparent,
        modifier = Modifier.background(gradient)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(title = "Playlists", showArrow = true)
                Spacer(modifier = Modifier.height(16.dp))
                PlaylistsRow(playlists = playlists)
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                SectionHeader(title = "Favourite", showArrow = false)
                Spacer(modifier = Modifier.height(20.dp))
            }

            items(favouriteSongs) { song ->
                FavouriteSongItem(song = song)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun SectionHeader(title: String, showArrow: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "View All",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "View All",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun PlaylistsRow(playlists: List<Playlist>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(playlists) { playlist ->
            PlaylistCard(playlist = playlist, isHighlighted = playlist.name == "Clear Mind")
        }
    }
}

@Composable
fun PlaylistCard(playlist: Playlist, isHighlighted: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = playlist.imageRes),
            contentDescription = playlist.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .then(
                    if (isHighlighted) {
                        Modifier.border(2.dp, Color(0xFF6C38A8), RoundedCornerShape(20.dp))
                    } else Modifier
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = playlist.name,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun FavouriteSongItem(song: Song) {
    // 1. Remember the favorite state for each item
    var isFavorited by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = song.imageRes),
            contentDescription = song.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = song.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = song.artist, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
        }
        // 2. Toggle the state on click
        IconButton(onClick = { isFavorited = !isFavorited }) {
            Icon(
                // 3. Change icon based on state
                imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favourite",
                // 4. Change color based on state
                tint = if (isFavorited) Color.Red else Color.White
            )
        }
    }
}


@Composable
fun BottomNavBar() {
    val navItems = listOf("Home", "Search", "Your Library")

    // Using the style and structure from your HomeScreen code
    NavigationBar(containerColor = Color(0xFF4C005F)) { // Using the purple from HomeScreen
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (index) {
                        0 -> Icon(Icons.Default.Home, contentDescription = "Home")
                        1 -> Icon(Icons.Default.Search, contentDescription = "Search")
                        2 -> Icon(Icons.Default.LibraryMusic, contentDescription = "Library") // Icon from HomeScreen
                    }
                },
                label = { Text(item) },
                // Set 'selected' to true for the "Your Library" item (index 2)
                selected = index == 2,
                onClick = { /* Handle navigation */ },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Transparent // No indicator bubble
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Playlist() {
    PlaylistScreen()
}