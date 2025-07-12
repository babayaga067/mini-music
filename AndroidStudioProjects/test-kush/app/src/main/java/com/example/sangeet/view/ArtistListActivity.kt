package com.example.sangeet.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sangeet.viewmodel.ArtistViewModel
import com.example.sangeet.model.ArtistModel
import com.example.sangeet.repository.ArtistRepositoryImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistListScreen(navController: NavController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4A004A),
            Color(0xFF1C0038)
        )
    )
    
    val repo = remember { ArtistRepositoryImpl() }
    val artistViewModel = remember { ArtistViewModel(repo) }
    
    val allArtists by artistViewModel.allArtists.observeAsState(emptyList())
    val searchResults by artistViewModel.searchResults.observeAsState(emptyList())
    val isLoading by artistViewModel.isLoading.observeAsState(false)
    
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        artistViewModel.getAllArtists()
    }
    
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            isSearching = true
            artistViewModel.searchArtists(searchQuery)
        } else {
            isSearching = false
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        TopAppBar(
            title = {
                Text(
                    "Artists",
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
        
        // Search Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.3f)
            )
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search artists...", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
        }
        
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFE91E63)
                    )
                }
            }
            
            isSearching && searchResults.isEmpty() && searchQuery.isNotBlank() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No artists found",
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                        Text(
                            "Try searching with different keywords",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            
            (!isSearching && allArtists.isEmpty()) -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No artists available",
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                        Text(
                            "Artists will appear here once added",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            
            else -> {
                val artistsToShow = if (isSearching) searchResults else allArtists
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(artistsToShow) { artist ->
                        ArtistItem(
                            artist = artist,
                            onClick = {
                                // Navigate to artist detail screen
                                // navController.navigate("artist_detail/${artist.artistId}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArtistItem(
    artist: ArtistModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Artist Image
            if (artist.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = artist.imageUrl,
                    contentDescription = "Artist Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Artist Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = artist.artistName,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                if (artist.genre.isNotBlank()) {
                    Text(
                        text = artist.genre,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                
                Text(
                    text = "${artist.followersCount} followers • ${artist.musicIds.size} songs",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}