package com.example.sangeet.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sangeet.model.UserModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SidebarDrawer(
    navController: NavController,
    onClose: () -> Unit,
    currentUser: UserModel?
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460))
    )

    val userId = currentUser?.userId ?: "guest"

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(gradient)
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // 🧑 User Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("profile/$userId"); onClose() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentUser?.fullName?.firstOrNull()?.uppercase() ?: "U",
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = currentUser?.fullName ?: "User Name",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currentUser?.email ?: "user@example.com",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Color.White.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))

        val navigationItems = listOf(
            SidebarItem("Home", Icons.Default.Home, "dashboard"),
            SidebarItem("Upload Music", Icons.Default.CloudUpload, "upload_music/$userId"),
            SidebarItem("My Favorites", Icons.Default.Favorite, "favorites/$userId"),
            SidebarItem("My Playlists", Icons.Default.PlaylistPlay, "playlists/$userId"),
            SidebarItem("Library", Icons.Default.LibraryMusic, "library"),
            SidebarItem("Search", Icons.Default.Search, "search"),
            SidebarItem("Settings", Icons.Default.Settings, "settings"),
            SidebarItem("About Us", Icons.Default.Info, "about_us")
        )

        navigationItems.forEach { item ->
            SidebarNavigationItem(item = item) {
                navController.navigate(item.route)
                onClose()
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Divider(color = Color.White.copy(alpha = 0.2f))

        SidebarNavigationItem(
            item = SidebarItem("Logout", Icons.Default.ExitToApp, "logout")
        ) {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
            onClose()
        }
    }
}

@Composable
fun SidebarNavigationItem(item: SidebarItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

data class SidebarItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)