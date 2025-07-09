package com.example.sangeet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sangeet.component.SplashScreen
import com.example.sangeet.view.*
import com.example.sangeet.viewmodel.SongViewModel
import com.example.sangeet.viewmodel.UserViewModel
import io.appwrite.Client

@Composable
fun AppNavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    songViewModel: SongViewModel,
    client: Client
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController, client = client)
        }
        composable("login") {
            LoginScreen(navController = navController, userViewModel = userViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController, userViewModel = userViewModel)
        }
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }
        composable("search") {
            SearchScreen(navController = navController)
        }
        composable("library") {
            LibraryScreen(navController = navController)
        }
        composable("playlist") {
            PlaylistScreen(navController = navController)
        }
        composable("menu") {
            MenuScreen(navController = navController)
        }
        composable("playingNow") {
            PlayingNowScreen(navController = navController, viewModel = songViewModel)
        }
    }
}