package com.example.musicplayer.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.R

@Composable
fun HomeScreen() {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4C005F), // Deep Purple
            Color(0xFF9D00B7)  // Vivid Violet
        ),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    val navItems = listOf("Home", "Search", "Your Library")

    val recentlyPlayed = listOf(
        "Stay" to R.drawable.stay,
        "Lover" to R.drawable.lover,
        "Upahaar" to R.drawable.upahaar,
        "Savage Love" to R.drawable.savage
    )

    val recommended = listOf(
        Triple("Mayalu", "VEK and Yabesh Thapa", R.drawable.mayalu),
        Triple("Farkanna Hola", "John Chamling Rai", R.drawable.farkanna),
        Triple("Jhim Jhumaune Aankha", "Ekdev Limbu", R.drawable.jhim),
        Triple("Apna Bana Le", "Arijit Singh & Sachin-Jigar", R.drawable.apna),
        Triple("Cruel Summer", "Taylor Swift", R.drawable.cruel)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF4C005F),
                contentColor = Color.White
            ) {
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
                        selected = index == 0, // Update as needed for active tab
                        onClick = { /* TODO: handle navigation */ },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.LightGray,
                            selectedTextColor = Color.LightGray,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
,

        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)

                    Column {
                        Text(
                            text = "Hi Eva,",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Good Afternoon",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("E", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Recently Played",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow {
                    items(recentlyPlayed) { item ->
                        val (title, image) = item
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = image),
                                contentDescription = title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(title, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Recommendation",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(recommended) { item ->
                        val (title, artist, image) = item
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = image),
                                contentDescription = title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(title, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(artist, color = Color.LightGray, fontSize = 12.sp)
                            }
                            Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
