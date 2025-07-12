package com.example.sangeet.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sangeet.viewmodel.FavoriteViewModel
import com.example.sangeet.model.MusicModel
import com.example.sangeet.repository.FavoriteRepositoryImpl
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, userId: String) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4A004A),
            Color(0xFF1C0038)
        )
    )
    
    val repo = remember { FavoriteRepositoryImpl() }
    val favoriteViewModel = remember { FavoriteViewModel(repo) }
    val context = LocalContext.current
    
    val favoriteMusics by favoriteViewModel.favoriteMusics.observeAsState(emptyList())
    val isLoading by favoriteViewModel.isLoading.observeAsState(false)
    
    LaunchedEffect(userId) {
        favoriteViewModel.getUserFavoriteMusics(userId)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        TopAppBar(
            title = {
                Text(
                    "My Favorites",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFE91E63))
            }
        } else if (favoriteMusics.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "No Favorites",
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No favorite songs yet",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                    Text(
                        "Start adding songs to your favorites!",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteMusics) { music ->
                    FavoriteMusicItem(
                        music = music,
                        onPlayClick = {
                            Toast.makeText(context, "Playing ${music.musicName}", Toast.LENGTH_SHORT).show()
                        },
                        onRemoveFromFavorites = {
                            favoriteViewModel.removeFromFavorites(userId, music.musicId) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteMusicItem(
    music: MusicModel,
    onPlayClick: () -> Unit,
    onRemoveFromFavorites: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = if (music.imageUrl.isNotEmpty()) {
                    if (music.imageUrl.startsWith("/")) File(music.imageUrl) else music.imageUrl
                } else {
                    "https://via.placeholder.com/60"
                },
                contentDescription = "Album Art",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = music.musicName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = music.artistName.ifEmpty { "Unknown Artist" },
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                if (music.genre.isNotEmpty()) {
                    Text(
                        text = music.genre,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
            
            IconButton(
                onClick = onPlayClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFFE91E63),
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(onClick = onRemoveFromFavorites) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Remove from Favorites",
                    tint = Color(0xFFE91E63)
                )
            }
        }
    }
}