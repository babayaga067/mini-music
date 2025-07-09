package com.example.sangeet.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String,
    modifier: Modifier = Modifier // ✅ Added modifier parameter
) {
    NavigationBar(
        modifier = modifier, // ✅ Applied here
        containerColor = Color(0xFF3D0E5C),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "dashboard",
            onClick = {
                if (currentRoute != "dashboard") {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = currentRoute == "search",
            onClick = {
                if (currentRoute != "search") {
                    navController.navigate("search") {
                        popUpTo("search") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.LibraryMusic, contentDescription = "Library") },
            label = { Text("Library") },
            selected = currentRoute == "library",
            onClick = {
                if (currentRoute != "library") {
                    navController.navigate("library") {
                        popUpTo("library") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}