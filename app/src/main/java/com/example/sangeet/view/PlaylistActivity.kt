package com.example.sangeet.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sangeet.component.BottomNavigationBar

@Composable
fun PlaylistScreen(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: ""
    val gradient = Brush.verticalGradient(listOf(Color(0xFF240046), Color(0xFF5A189A)))

    var showDialog by remember { mutableStateOf(false) }

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

        // ✅ Floating Action Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF9D4EDD),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Playlist")
            }
        }

        // ✅ Bottom Navigation
        BottomNavigationBar(navController = navController, currentRoute = currentRoute)

        // ✅ Dialog
        if (showDialog) {
            CreatePlaylistDialog(
                onDismiss = { showDialog = false },
                onCreate = { playlistName ->
                    showDialog = false
                    // TODO: Save playlist to backend or local list
                    println("Created playlist: $playlistName")
                }
            )
        }
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
        "Clear Mind" to "https://i.imgur.com/1Yc9yOE.png",
        "Sound of Nature" to "https://i.imgur.com/2Kyj3cF.png",
        "Relax Songs" to "https://i.imgur.com/X9tD1Ad.png"
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
        songs.forEach { (title, artist, imageUrl) ->
            RecentlyPlayedItem(title = title, artist = artist, imageUrl = imageUrl)
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
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun CreatePlaylistDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var playlistName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (playlistName.isNotBlank()) onCreate(playlistName)
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("New Playlist") },
        text = {
            TextField(
                value = playlistName,
                onValueChange = { playlistName = it },
                placeholder = { Text("Enter playlist name") },
                singleLine = true
            )
        },
        containerColor = Color.White
    )
}