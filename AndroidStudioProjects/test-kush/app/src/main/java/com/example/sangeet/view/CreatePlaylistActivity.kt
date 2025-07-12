package com.example.sangeet.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sangeet.viewmodel.PlaylistViewModel
import com.example.sangeet.model.PlaylistModel
import com.example.sangeet.repository.PlaylistRepositoryImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistScreen(
    navController: NavController,
    userId: String)
    {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A004A), Color(0xFF1C0038))
    )

    val context = LocalContext.current
    val playlistViewModel = remember {
        PlaylistViewModel(PlaylistRepositoryImpl())
    }

    var playlistName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(false) }
    var isCreating by remember { mutableStateOf(false) }
    var creationSuccess by remember { mutableStateOf(false) }

    if (creationSuccess) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        TopAppBar(
            title = {
                Text("Create Playlist", color = Color.White, fontWeight = FontWeight.Bold)
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Playlist Details", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = playlistName,
                        onValueChange = { playlistName = it },
                        label = { Text("Playlist Name") },
                        leadingIcon = {
                            Icon(Icons.Default.PlaylistAdd, contentDescription = null, tint = Color.White)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = customFieldColors()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = customFieldColors()
                    )

                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("Cover Image Path (Optional)") },
                        leadingIcon = {
                            Icon(Icons.Default.Image, contentDescription = null, tint = Color.White)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = customFieldColors()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Make playlist public", color = Color.White, fontSize = 16.sp)
                        Switch(
                            checked = isPublic,
                            onCheckedChange = { isPublic = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFFE91E63),
                                checkedTrackColor = Color(0xFFE91E63).copy(alpha = 0.5f)
                            )
                        )
                    }

                    Text(
                        if (isPublic) "Other users can see and follow this playlist"
                        else "Only you can see this playlist",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Button(
                onClick = {
                    if (playlistName.isBlank()) {
                        Toast.makeText(context, "Please enter a playlist name", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isCreating = true
                    val playlist = PlaylistModel(
                        userId = userId,
                        playlistName = playlistName,
                        description = description,
                        imageUrl = imageUrl,
                        isPublic = isPublic
                    )

                    playlistViewModel.createPlaylist(playlist) { success, message ->
                        isCreating = false
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        if (success) creationSuccess = true
                    }
                },
                enabled = !isCreating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                if (isCreating) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Icon(Icons.Default.PlaylistAdd, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Playlist", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun customFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFFE91E63),
    unfocusedBorderColor = Color.Gray,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.Gray,
    cursorColor = Color.White,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White
)