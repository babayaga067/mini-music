package com.example.sangeet.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sangeet.model.MusicModel
import com.example.sangeet.repository.MusicRepositoryImpl
import com.example.sangeet.repository.UserRepositoryImpl
import com.example.sangeet.utils.FileUploadUtil
import com.example.sangeet.viewmodel.MusicViewModel
import com.example.sangeet.viewmodel.UserViewModel
import java.util.UUID


class UploadMusicActivity : ComponentActivity() {
    private val userViewModel by lazy {
        UserViewModel(UserRepositoryImpl()) }
    private val musicViewModel by lazy {
        MusicViewModel(MusicRepositoryImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userId = intent.getStringExtra("userId") ?: ""
        setContent {
            val navController = rememberNavController()
            UploadMusicScreen(
                navController = navController,
                userId = userId,
                userViewModel = userViewModel,
                musicViewModel = musicViewModel)}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadMusicScreen(navController: NavController, userId: String,musicViewModel: MusicViewModel, userViewModel : UserViewModel) {
    val context = LocalContext.current
    val gradient = Brush.verticalGradient(listOf(Color(0xFF4A004A), Color(0xFF1C0038)))
    val musicViewModel = remember { MusicViewModel(MusicRepositoryImpl()) }

    var musicName by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val audioPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> audioUri = uri }
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> imageUri = uri }


    val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }
    Column(modifier = Modifier.fillMaxSize().background(gradient)) {
        TopAppBar(
            title = { Text("Upload Music", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // 🎼 Music Metadata Card
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f))) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    @Composable
                    fun fieldColors() = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color(0xFFE91E63),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray
                    )

                    Text("Music Details", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    OutlinedTextField(value = musicName, onValueChange = { musicName = it }, label = { Text("Music Name") }, leadingIcon = { Icon(Icons.Default.MusicNote, null, tint = Color.White) }, modifier = Modifier.fillMaxWidth(), colors = fieldColors())
                    OutlinedTextField(value = artistName, onValueChange = { artistName = it }, label = { Text("Artist Name") }, modifier = Modifier.fillMaxWidth(), colors = fieldColors())
                    OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Genre") }, modifier = Modifier.fillMaxWidth(), colors = fieldColors())
                    OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration (seconds)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), colors = fieldColors())
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3, colors = fieldColors())
                }
            }

            // 🎵 File Picker Card
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f))) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("File Selection", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    Row(modifier = Modifier.fillMaxWidth().clickable { audioPicker.launch("audio/*") }.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MusicNote, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (audioUri == null) "Select Audio File" else audioUri!!.lastPathSegment ?: "Audio Selected", color = Color.White)
                    }

                    Row(modifier = Modifier.fillMaxWidth().clickable { imagePicker.launch("image/*") }.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Image, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (imageUri == null) "Select Cover Image" else imageUri!!.lastPathSegment ?: "Image Selected", color = Color.White)
                    }

                    Text("Note: Files will be uploaded to backend using Firebase.", color = Color.Gray, fontSize = 12.sp)
                }
            }

            // 🚀 Upload Button
            Button(
                onClick = {
                    if (musicName.isBlank() || artistName.isBlank() || audioUri == null) {
                        Toast.makeText(context, "Please fill required fields and select audio", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isUploading = true
                    val musicId = UUID.randomUUID().toString()

                    FileUploadUtil.uploadAudio(context, audioUri!!) { audioSuccess, audioUrl ->
                        if (audioSuccess && audioUrl != null) {
                            val finalize = { imageUrl: String ->
                                val music = MusicModel(
                                    musicId = musicId,
                                    musicName = musicName,
                                    artistName = artistName,
                                    genre = genre,
                                    description = description,
                                    duration = duration.toLongOrNull() ?: 0L,
                                    audioUrl = audioUrl,
                                    imageUrl = imageUrl,
                                    uploadedBy = userId,
                                    uploadedAt = System.currentTimeMillis()
                                )
                                musicViewModel.addMusic(music) { success, message ->
                                    isUploading = false
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    if (success) navController.popBackStack()
                                }
                            }

                            if (imageUri != null) {
                                FileUploadUtil.uploadImage(context, imageUri!!) { imageSuccess, imageUrl ->
                                    finalize(if (imageSuccess && imageUrl != null) imageUrl else "")
                                }
                            } else {
                                finalize("")
                            }
                        } else {
                            isUploading = false
                            Toast.makeText(context, "Failed to upload audio", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                if (isUploading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload Music", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}