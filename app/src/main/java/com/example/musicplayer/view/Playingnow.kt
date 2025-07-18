package com.example.musicplayer.view
//import

import androidx.compose.foundation.Image
//error
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
//e3e
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//music players -


class PlayingnowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayingNowScreen()
            //playingnowscreen
        }
    }
}


@Composable
//playingnow
//Composable
fun PlayingNowScreen() {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4C005F), Color(0xFF9D00B7)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    val moreSongs = listOf(
        Triple("Blue", "Young Kai", R.drawable.cruel),
        Triple("Cruel Summer", "Taylor Swift", R.drawable.cruel)
    )
    //Scaffold



    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4C005F), contentColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = {}
                    //on click
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") },
                    selected = false,
                    onClick = {}
                    //group
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LibraryMusic, contentDescription = "Library") },
                    label = { Text("Your Library") },
                    selected = true,
                    onClick = {}
                )
            }
        }
    ) { padding ->
        Box(
            //box
            //Box
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradient)
                .padding(padding)
        ) {
            Column(

                //Column
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top App Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Playing now",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Album Art
                Image(
                    painter = painterResource(id = R.drawable.cruel),
                    contentDescription = "Album Art",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))
                //spacer

                // Song Info
                Text("Night Changes", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("One Direction", color = Color.LightGray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Bar
                Slider(
                    value = 0.65f,
                    onValueChange = {},
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.LightGray,
                        inactiveTrackColor = Color.Gray
                    )
                )

                // Time Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("2:34", color = Color.White, fontSize = 12.sp)
                    Text("3:45", color = Color.White, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = Color.White)
                    Text("-10s", color = Color.White)
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.PauseCircle, contentDescription = "Pause", tint = Color.White, modifier = Modifier.size(50.dp))
                    }
                    Text("+10s", color = Color.White)
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Divider(color = Color.LightGray.copy(alpha = 0.4f))

                Spacer(modifier = Modifier.height(12.dp))

                // More Songs
                Text("More Songs", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(8.dp))
// Column
                LazyColumn {
                    items(moreSongs) { item ->
                        val (title, artist, image) = item
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = image),
                                contentDescription = title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                //clip
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
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
//preview
@Composable
fun Previewplayingnow() {
    //preview

}
