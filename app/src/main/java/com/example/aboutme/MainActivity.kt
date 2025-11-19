package com.example.aboutme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aboutme.ui.theme.AboutmeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.delay

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
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashScreen(
                                onTimeout = {
                                    navController.navigate("home") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

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

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF050816),
                        Color(0xFF090B20),
                        Color(0xFF050816)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(999.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "KuriZd Portfolio",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.95f)
            )

            Text(
                text = "Powered by Jetpack Compose",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}
