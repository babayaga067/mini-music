package com.example.musicplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.ComposableTargetMarker
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun SearchScreen() {
    val gradientColors = listOf(Color(0xFF6100FF), Color(0xFF9A00FF))
    var searchQuery by remember { mutableStateOf() }
    val songs = remember {
        mutableStateOf(
            Song("Easy On Me", "Adelle", R.drawable.easy_on_me),
            Song("Blue", "Yung Kai", R.drawable.blue),
            Song("Dandelions", "Ruth B", R.drawable.dandelions),
            Song("Upahaar", "Swopna Suman", R.drawable.upahaar, true),
            Song("Jhim Jhumaune Aankha", "Ekdev Limbu", R.drawable.jhim),
            Song("Apna Bana Le", "Arijit Singh", R.drawable.apna_bana_le),
            Song("Furfuri", "Unknown", R.drawable.furfuri)
        )
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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                Spacer(modifier = Modifier.weight(1f))
                Text("Search", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
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

            Spacer(modifier = Modifier.height(16.dp))

            // Search bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search here", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.White,
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
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(songs) { song ->
                    SongItem(song = song) {
                        song.isFavorite = !song.isFavorite
                    }
                }
            }
        }
    }
}

data class Song(
    val title: String,
    val artist: String,
    val imageRes: Int,
    var isFavorite: Boolean = false
)

@Composable
fun SongItem(song: Song, onHeartClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = song.imageRes),
            contentDescription = song.title,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(song.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(song.artist, color = Color.LightGray, fontSize = 12.sp)
        }

        IconButton { }(onClick = onHeartClick) {
            Icon(
                imageVector = if (song.isFavorite) Icon().Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (song.isFavorite) Color.White else Color.LightGray
            )
        }
    }
}


