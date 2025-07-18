package com.example.musicplayer.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.musicplayer.R

class MyLibraryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibraryScreen()
        }
    }
}

@Composable
fun LibraryScreen() {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF5B0E9C), Color(0xFF27005D))
    )

    Scaffold(
        bottomBar = { BottomNavBar3() },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Hi Eva,", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text("Good Afternoon", color = Color.White, fontSize = 14.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0D3F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("E", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Your Library", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LibraryCard(icon = Icons.Default.Favorite, title = "Favourite", subtitle = "29 songs")
                    LibraryCard(icon = Icons.Default.PlaylistPlay, title = "Playlists", subtitle = "4 playlists")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recently Played", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Icon(Icons.Default.ExpandMore, contentDescription = null, tint = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                val recentlyPlayed = listOf(
                    Song("Dandelions", "Ruth B", R.drawable.cruel),
                    Song("Blue", "Vung Kai", R.drawable.cruel),
                    Song("Night Changes", "One Direction", R.drawable.cruel),
                    Song("Dhaiyra", "Sajan Raj Vaidya", R.drawable.cruel),
                    Song("Sarara Sarara", "Kefir Singh Limba", R.drawable.cruel),
                    Song("Furfuri", "", R.drawable.cruel)
                )

                LazyColumn {
                    items(recentlyPlayed) { song ->
                        SongItem(song)
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryCard(icon: ImageVector, title: String, subtitle: String) {
    Card(
        modifier = Modifier
//            .weight(1f)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x40FFFFFF))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null, tint = Color.White)
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

data class Song(
    val title: String,
    val artist: String,
    val imageRes: Int
)


@Composable
fun SongItem(song: Song) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = song.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(song.title, color = Color.White, fontWeight = FontWeight.Medium)
            Text(song.artist, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
        Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White)
    }
}



@Composable
fun BottomNavBar3() {
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
                selected = index == 3,
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
fun PreviewLibraryScreen() {

}

