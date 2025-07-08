package com.example.sangeet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sangeet.component.SplashScreen

import com.example.sangeet.ui.theme.SangeetTheme
import com.example.sangeet.view.DashboardScreen
import com.example.sangeet.view.LoginScreen
import com.example.sangeet.view.PlaylistScreen
import com.example.sangeet.view.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SangeetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("dashboard") {
            DashboardScreen(navController = navController)
        }

        composable("search") {
            SearchScreen(navController = navController)
        }

        composable("playlist") {
            PlaylistScreen(navController = navController)
        }

        composable("playing_now") {
            PlayingNowScreen(navController = navController)
        }

        composable("menu") {
            MenuScreen(navController = navController)
        }

        composable("library") {
            LibraryScreen(navController = navController)
        }
    }
}