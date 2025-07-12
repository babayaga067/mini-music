package com.example.sangeet.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sangeet.component.SplashScreen
import com.example.sangeet.repository.*
import com.example.sangeet.view.*
import com.example.sangeet.viewmodel.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    // 🔁 Shared ViewModels (remembered in graph scope)
    val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }
    val musicViewModel = remember { MusicViewModel(MusicRepositoryImpl()) }
    val favoriteViewModel = remember { FavoriteViewModel(FavoriteRepositoryImpl()) }
    val playlistViewModel = remember { PlaylistViewModel(PlaylistRepositoryImpl()) }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 🟡 Auth
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController, userViewModel) }
        composable("register") { RegisterScreen(navController, userViewModel) }

        // 🟢 Main Dashboard
        composable("dashboard") {
            DashboardScreen(
                navController = navController,
                userViewModel = userViewModel,
                musicViewModel = musicViewModel,
                favoriteViewModel = favoriteViewModel,
                playlistViewModel = playlistViewModel
            )
        }

        // 🔍 Utilities
        composable("search") { SearchScreen(navController) }
        composable("library") { LibraryScreen() }
        composable("menu") { MenuScreen() }

        // 👤 Profile
        composable(
            route = "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId").orEmpty()
            ProfileScreen(navController, userId, musicViewModel, userViewModel)
        }

        composable(
            route = "edit_profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId").orEmpty()
            EditProfileScreen(navController, userId)
        }

        // 🎵 Music Upload & Playback
        composable(
            route = "upload_music/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId").orEmpty()
            UploadMusicScreen(navController, userId, musicViewModel, userViewModel)
        }

        composable(
            route = "playing_now/{musicId}",
            arguments = listOf(navArgument("musicId") { type = NavType.StringType })
        ) {
            val musicId = it.arguments?.getString("musicId").orEmpty()
            PlayingNowScreen(musicId, musicViewModel, navController)
        }

        // 🎧 Favorites & Playlists
        composable(
            route = "favorites/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId").orEmpty()
            FavoritesScreen(navController, userId)
        }

        composable(
            route = "playlists/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId").orEmpty()
            PlaylistScreen(navController, userId)
        }

        composable(
            route = "create_playlist/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId").orEmpty()
            CreatePlaylistScreen(navController, userId)
        }

        // 🎤 Artists
        composable("artists") { ArtistListScreen(navController) }

        // 📄 Informational Pages
        composable("about_us") { AboutUsScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}