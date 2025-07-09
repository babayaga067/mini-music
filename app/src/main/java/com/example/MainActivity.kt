package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.sangeet.navigation.AppNavGraph
import com.example.sangeet.repository.UserRepositoryImpl
import com.example.sangeet.ui.theme.SangeetTheme
import com.example.sangeet.viewmodel.SongViewModel
import com.example.sangeet.viewmodel.UserViewModel
import io.appwrite.Client

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Initialize App write Client
        val appwriteClient = Client(this)
            .setEndpoint("https://cloud.appwrite.io/v1") // Replace with your endpoint
            .setProject("your-project-id")               // Replace with your project ID

        //  Create Repositories and ViewModels
        val userRepo = UserRepositoryImpl(
            client = appwriteClient,
            databaseId = "your-database-id",             // Replace with your DB ID
            collectionId = "your-user-collection-id"     // Replace with your collection ID
        )
        val userViewModel = UserViewModel(userRepo)
        val songViewModel = SongViewModel(appwriteClient)

        setContent {
            SangeetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavGraph(
                        navController = navController,
                        userViewModel = userViewModel,
                        songViewModel = songViewModel,
                        client = appwriteClient
                    )
                }
            }
        }
    }
}