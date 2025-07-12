package com.example.sangeet.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sangeet.component.*
import com.example.sangeet.model.MusicModel
import com.example.sangeet.utils.toggleFavorite
import com.example.sangeet.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    musicViewModel: MusicViewModel,
    favoriteViewModel: FavoriteViewModel,
    playlistViewModel: PlaylistViewModel
) {
    val gradient = Brush.verticalGradient(listOf(Color(0xFF4A004A), Color(0xFF1C0038)))
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val authUserId = remember { FirebaseAuth.getInstance().currentUser?.uid }

    if (authUserId == null) {
        ShowLoginRequiredScreen(gradient, drawerState)
        return
    }

    val context = LocalContext.current

    val allMusics by musicViewModel.allMusics.observeAsState(emptyList())
    val isLoading by musicViewModel.isLoading.observeAsState(false)
    val currentUser by userViewModel.user.observeAsState()
    val favoriteMusics by favoriteViewModel.favoriteMusics.observeAsState(emptyList())
    val userPlaylists by playlistViewModel.userPlaylists.observeAsState(emptyList())

    var showPlaylistDialog by remember { mutableStateOf(false) }
    var selectedMusicForPlaylist by remember { mutableStateOf<MusicModel?>(null) }

    LaunchedEffect(Unit) {
        musicViewModel.getAllMusics()
        userViewModel.getUserById(authUserId)
        favoriteViewModel.getUserFavoriteMusics(authUserId)
        playlistViewModel.getUserPlaylists(authUserId)
    }

    val recentlyPlayed = allMusics.takeLast(4)
    val recommended = allMusics.take(10)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SidebarDrawer(
                navController = navController,
                onClose = { scope.launch { drawerState.close() } },
                currentUser = currentUser
            )
        }
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar() },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(gradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    DashboardTopBar(
                        currentUser = currentUser,
                        onRefresh = {
                            musicViewModel.getAllMusics()
                            userViewModel.getUserById(authUserId)
                            favoriteViewModel.getUserFavoriteMusics(authUserId)
                            playlistViewModel.getUserPlaylists(authUserId)
                            Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT).show()
                        },
                        onProfileClick = {
                            navController.navigate("profile/$authUserId")
                        },
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    QuickAccessSection(navController, authUserId)
                    Spacer(modifier = Modifier.height(24.dp))

                    RecentlyPlayedSection(
                        musics = recentlyPlayed.filterNotNull(),
                        isLoading = isLoading,
                        userId = authUserId,
                        favoriteMusics = favoriteMusics,
                        onToggleFavorite = { musicId ->
                            toggleFavorite(authUserId, musicId, favoriteViewModel, context)
                        },
                        onAddToPlaylist = { music ->
                            selectedMusicForPlaylist = music
                            showPlaylistDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RecommendationSection(
                        musics = recommended.filterNotNull(),
                        isLoading = isLoading,
                        userId = authUserId,
                        favoriteMusics = favoriteMusics,
                        onToggleFavorite = { musicId ->
                            toggleFavorite(authUserId, musicId, favoriteViewModel, context)
                        },
                        onAddToPlaylist = { music ->
                            selectedMusicForPlaylist = music
                            showPlaylistDialog = true
                        }
                    )
                }

                selectedMusicForPlaylist?.let { music ->
                    if (showPlaylistDialog) {
                        AddToPlaylistDialog(
                            music = music,
                            playlists = userPlaylists,
                            onDismiss = {
                                showPlaylistDialog = false
                                selectedMusicForPlaylist = null
                            },
                            onCreateNew = {
                                navController.navigate("create_playlist/$authUserId")
                                showPlaylistDialog = false
                                selectedMusicForPlaylist = null
                            },
                            onAddToPlaylist = { playlistId ->
                                playlistViewModel.addMusicToPlaylist(
                                    playlistId = playlistId,
                                    musicId = music.musicId
                                ) { success, message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                                showPlaylistDialog = false
                                selectedMusicForPlaylist = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowLoginRequiredScreen(
    gradient: Brush,
    drawerState: DrawerState
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {}
    ) {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(gradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Please log in to continue",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}