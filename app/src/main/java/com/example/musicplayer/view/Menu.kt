package com.example.musicplayer
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
@Composable
fun MenuScreen() {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4B0082), Color(0xFF8A2BE2)) // Deep purple gradient
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TopBar()
            Spacer(modifier = Modifier.height(32.dp))
            Text("Welcome  back!", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            MenuButton("Home")
            MenuButton("Profile")
            MenuButton("Help and Support")
        }

        BottomNavBar(Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
        Text("Menu", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(24.dp)) // For symmetry with menu icon
    }
}

@Composable
fun MenuButton(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun BottomNavBar(modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color(0x55000000)
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text("Search") }
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.LibraryMusic, contentDescription = null) },
            label = { Text("Your Library") }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewMenuScreen() {
    LoginBody()
}

