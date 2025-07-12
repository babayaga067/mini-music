package com.example.sangeet.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import coil.compose.AsyncImage
import java.io.File
import com.example.sangeet.viewmodel.FavoriteViewModel
import com.example.sangeet.viewmodel.MusicViewModel
import com.example.sangeet.repository.FavoriteRepositoryImpl
import com.example.sangeet.repository.MusicRepositoryImpl
import com.example.sangeet.model.MusicModel

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
fun SearchScreen(navController: NavController? = null) {
    val gradientColors = listOf(Color(0xFF6100FF), Color(0xFF9A00FF))
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    // Initialize repositories and view models
    val musicRepo = remember { MusicRepositoryImpl() }
    val favoriteRepo = remember { FavoriteRepositoryImpl() }
    val musicViewModel = remember { MusicViewModel(musicRepo) }
    val favoriteViewModel = remember { FavoriteViewModel(favoriteRepo) }

    // Observe data from view models
    val allMusics by musicViewModel.allMusics.observeAsState(emptyList())
    val favoriteMusics by favoriteViewModel.favoriteMusics.observeAsState(emptyList())

    // Initialize searchQuery with an empty string
    var searchQuery by remember { mutableStateOf("") }

    // Load data when screen is first composed
    LaunchedEffect(Unit) {
        musicViewModel.getAllMusics()
        if (userId.isNotEmpty()) {
            favoriteViewModel.getUserFavoriteMusics(userId)
        }
    }

    // Filter musics based on search query
    val filteredMusics = allMusics.filterNotNull().filter { music ->
        music.musicName.contains(searchQuery, ignoreCase = true) ||
                music.artistName.contains(searchQuery, ignoreCase = true) ||
                music.genre.contains(searchQuery, ignoreCase = true)
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
                IconButton(onClick = { 
                    navController?.navigateUp() ?: (context as? Activity)?.finish()
                }) {
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

            if (allMusics.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (filteredMusics.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results found", color = Color.White.copy(0.7f))
                }
            } else {
                LazyColumn {
                    items(filteredMusics) { music ->
                        val isFavorite = favoriteMusics.any { it.musicId == music.musicId }
                        MusicItem(
                            music = music,
                            isFavorite = isFavorite,
                            onHeartClick = {
                                if (userId.isNotEmpty()) {
                                    favoriteViewModel.toggleFavorite(userId, music.musicId) { success, message ->
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Please login to add favorites", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun MusicItem(
    music: MusicModel,
    isFavorite: Boolean,
    onHeartClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (music.imageUrl.isNotEmpty()) {
                if (music.imageUrl.startsWith("/")) File(music.imageUrl) else music.imageUrl
            } else {
                "https://via.placeholder.com/50"
            },
            contentDescription = music.musicName,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = music.musicName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = music.artistName.ifEmpty { "Unknown Artist" },
                color = Color.LightGray,
                fontSize = 12.sp
            )
            if (music.genre.isNotEmpty()) {
                Text(
                    text = music.genre,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }

        IconButton(onClick = onHeartClick) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color(0xFFE91E63) else Color.LightGray
            )
        }
    }
}