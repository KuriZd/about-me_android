package com.example.aboutme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.aboutme.ui.theme.AboutmeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AboutmeTheme {
                val navController: NavHostController = rememberNavController()

                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                            composable("home") {
                                AboutMeWithFluidNav(navController = navController)
                            }

                            composable("spotify_screen") {
                                SpotifyWithFluidNav(navController = navController)
                            }

                            composable("projects_gallery") {
                                ProjectsWithFluidNav(navController = navController)
                            }

                            composable("cv_screen") {
                                CvWithFluidNav(navController = navController)
                            }

                            composable(
                                route = "project_images/{projectId}",
                                arguments = listOf(
                                    navArgument("projectId") { type = NavType.IntType }
                                )
                            ) { backStackEntry ->
                                val projectId = backStackEntry.arguments?.getInt("projectId") ?: 0
                                ProjectImagesScreen(
                                    navController = navController,
                                    projectId = projectId
                                )
                            }
                    }
                }
            }
        }
    }
}
