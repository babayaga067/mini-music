package com.example.sangeet.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sangeet.component.SplashScreen
import com.example.sangeet.view.LoginScreen
import com.example.sangeet.view.DashboardScreen
import com.example.sangeet.view.LibraryScreen
import com.example.sangeet.view.RegisterScreen
import com.example.sangeet.view.SearchScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable ("search") { SearchScreen(navController) }


    }
}
