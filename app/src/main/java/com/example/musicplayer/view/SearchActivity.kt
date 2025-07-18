package com.example.musicplayer.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.R
import androidx.annotation.DrawableRes
import androidx.compose.ui.tooling.preview.Preview

data class RecentSearch(
    val id: Int,
    val title: String,
    val artist: String,
    val imageRes: Int
)


class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    val recentSearches = mutableListOf(
            RecentSearch(1, "Night Changes", "One Direction", R.drawable.cruel),
            RecentSearch(2, "Cruel Summer", "Taylor Swift", R.drawable.cruel),
            RecentSearch(3, "Blue", "Y!ung Kai", R.drawable.cruel)
        )


    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2A0D4E), Color(0xFF1B0C2B))
    )

    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Search",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recent Searches",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(recentSearches, key = { it.id }) { item ->
                    SearchItem(item = item) {
                        recentSearches.remove(item)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (recentSearches.isNotEmpty()) {
                Text(
                    text = "Clear history",
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { recentSearches.clear() }
                        .padding(top = 12.dp),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SearchItem(item: RecentSearch, onRemove: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = item.artist, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White.copy(alpha = 0.7f))
        }
    }
}

// ✅ Reuse the BottomNavBar from your PlaylistActivity
@Composable
fun BottomNavBar2() {
    val navItems = listOf("Home", "Search", "Your Library")

    NavigationBar(containerColor = Color(0xFF4C005F)) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (index) {
                        0 -> Icon(Icons.Default.Home, contentDescription = "Home")
                        1 -> Icon(Icons.Default.Search, contentDescription = "Search")
                        2 -> Icon(Icons.Default.LibraryMusic, contentDescription = "Library")
                    }
                },
                label = { Text(item) },
                selected = index == 1, // Search is selected
                onClick = { /* Navigation logic */ },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}