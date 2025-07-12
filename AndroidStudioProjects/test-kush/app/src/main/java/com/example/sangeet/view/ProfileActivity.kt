package com.example.sangeet.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sangeet.model.UserModel
import com.example.sangeet.repository.MusicRepositoryImpl
import com.example.sangeet.repository.UserRepositoryImpl
import com.example.sangeet.viewmodel.MusicViewModel
import com.example.sangeet.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : ComponentActivity() {
    private val userViewModel by lazy {
        UserViewModel(UserRepositoryImpl()) }
    private val musicViewModel by lazy {
        MusicViewModel(MusicRepositoryImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getStringExtra("userId") ?: ""

        setContent {
            ProfileScreen(
                navController = rememberNavController(),
                userId = userId,
                musicViewModel = musicViewModel,
                userViewModel = userViewModel)
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController? = null,
                  userId: String,
                  musicViewModel: MusicViewModel,
                  userViewModel: UserViewModel) {
    val context = LocalContext.current
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1A2E),
            Color(0xFF16213E),
            Color(0xFF0F3460)
        )
    )
    
    val userRepository = UserRepositoryImpl()
//    val userViewModel = UserViewModel(UserRepositoryImpl())
    val currentUser by userViewModel.user.observeAsState()
    var isLoading by remember { mutableStateOf(false) }
    
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "user123"
    
    var showEditDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        if (userId != "user123") {
            userViewModel.getUserById(userId)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController?.navigateUp() }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFE91E63))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser?.fullName?.firstOrNull()?.toString()?.uppercase() ?: "U",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 48.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = currentUser?.fullName ?: "User",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = currentUser?.email ?: "user@example.com",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Profile Options
                    ProfileOptionCard(
                        icon = Icons.Default.Edit,
                        title = "Edit Profile",
                        subtitle = "Update your personal information",
                        onClick = { showEditDialog = true }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ProfileOptionCard(
                        icon = Icons.Default.Lock,
                        title = "Change Password",
                        subtitle = "Update your account password",
                        onClick = { showPasswordDialog = true }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ProfileOptionCard(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Manage your notification preferences",
                        onClick = { /* Handle notifications */ }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ProfileOptionCard(
                        icon = Icons.Default.Settings,
                        title = "Settings",
                        subtitle = "App preferences and configurations",
                        onClick = { /* Handle settings */ }
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Logout Button
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController?.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Logout",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
    
    // Edit Profile Dialog
    if (showEditDialog) {
        EditProfileDialog(
            currentUser = currentUser,
            onDismiss = { showEditDialog = false },
            onSave = { updatedUser ->
                isLoading = true
                val updates = mapOf(
                    "fullName" to updatedUser.fullName
                )
                userViewModel.updateProfile(userId, updates) { success, message ->
                    isLoading = false
                    if (success) {
                        userViewModel.getUserById(userId)
                    }
                }
                showEditDialog = false
            }
        )
    }
    
    // Change Password Dialog
    if (showPasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showPasswordDialog = false },
            onSave = { oldPassword, newPassword ->
                // Handle password change
                val user = FirebaseAuth.getInstance().currentUser
                user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Password updated successfully
                    }
                }
                showPasswordDialog = false
            }
        )
    }
}

@Composable
fun ProfileOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x40FFFFFF)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFFE91E63),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    currentUser: UserModel?,
    onDismiss: () -> Unit,
    onSave: (UserModel) -> Unit
) {
    var fullName by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edit Profile", color = Color.White)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color(0xFFE91E63),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.White.copy(alpha = 0.5f),
                        disabledLabelColor = Color.White.copy(alpha = 0.5f)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    currentUser?.let {
                        onSave(it.copy(fullName = fullName))
                    }
                }
            ) {
                Text("Save", color = Color(0xFFE91E63))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        },
        containerColor = Color(0xFF2A2A3E)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Change Password", color = Color.White)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                            Icon(
                                if (showCurrentPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color(0xFFE91E63),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showNewPassword = !showNewPassword }) {
                            Icon(
                                if (showNewPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color(0xFFE91E63),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color(0xFFE91E63),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (newPassword == confirmPassword && newPassword.isNotEmpty()) {
                        onSave(currentPassword, newPassword)
                    }
                },
                enabled = newPassword == confirmPassword && newPassword.isNotEmpty() && currentPassword.isNotEmpty()
            ) {
                Text("Change", color = Color(0xFFE91E63))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        },
        containerColor = Color(0xFF2A2A3E)
    )
}