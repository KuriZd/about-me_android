package com.example.aboutme

import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aboutme.ui.SpotifyScreen

@Composable
fun AboutMeWithFluidNav(
    navController: NavController
) {
    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "fabProgress"
    )

    val clickAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 350,
            easing = LinearEasing
        ),
        label = "clickProgress"
    )

    val renderEffect: RenderEffect? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getBlurRenderEffect()
        } else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232633))
    ) {
        AboutMeScreen(
            modifier = Modifier.let { base ->
                if (renderEffect != null && isMenuExtended.value) {
                    base.graphicsLayer { this.renderEffect = renderEffect }
                } else base
            },
            onCvClick = {
                navController.navigate("cv_screen") {
                    launchSingleTop = true
                }
            }
        )

        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 14.dp)
        ) {
            CustomBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            Circle(
                color = MaterialTheme.colorScheme.onBackground,
                animationProgress = clickAnimationProgress
            )

            FabGroup(
                animationProgress = fabAnimationProgress,
                toggleAnimation = { isMenuExtended.value = !isMenuExtended.value },
                onSpotifyClick = {
                    navController.navigate("spotify_screen") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                },
                onAboutClick = {
                    navController.navigate("home") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                },
                onProjectsClick = {
                    navController.navigate("projects_gallery") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                }
            )
        }
    }
}




@Composable
fun SpotifyWithFluidNav(
    navController: NavController
) {
    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "fabProgress"
    )

    val clickAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 350,
            easing = LinearEasing
        ),
        label = "clickProgress"
    )

    val renderEffect: RenderEffect? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getBlurRenderEffect()
        } else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232633))
    ) {
        SpotifyScreen(
            onBack = { navController.popBackStack() },
            modifier = Modifier.let { base ->
                if (renderEffect != null && isMenuExtended.value) {
                    base.graphicsLayer { this.renderEffect = renderEffect }
                } else base
            }
        )

        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 14.dp)
        ) {
            CustomBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            Circle(
                color = MaterialTheme.colorScheme.onBackground,
                animationProgress = clickAnimationProgress
            )

            FabGroup(
                animationProgress = fabAnimationProgress,
                toggleAnimation = { isMenuExtended.value = !isMenuExtended.value },
                onSpotifyClick = {
                    navController.navigate("spotify_screen") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                },
                onAboutClick = {
                    navController.navigate("home") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                },
                onProjectsClick = {
                    navController.navigate("projects_gallery") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                }
            )
        }
    }
}

@Composable
fun ProjectsWithFluidNav(
    navController: NavController
) {
    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "fabProgress"
    )

    val clickAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 350,
            easing = LinearEasing
        ),
        label = "clickProgress"
    )

    val renderEffect: RenderEffect? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getBlurRenderEffect()
        } else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232633))
    ) {
        ProjectsGalleryScreen(
            modifier = Modifier.let { base ->
                if (renderEffect != null && isMenuExtended.value) {
                    base.graphicsLayer { this.renderEffect = renderEffect }
                } else base
            },
            onProjectClick = { project ->
                navController.navigate("project_images/${project.id}")
            }
        )

        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 14.dp)
        ) {
            CustomBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            Circle(
                color = MaterialTheme.colorScheme.onBackground,
                animationProgress = clickAnimationProgress
            )

            FabGroup(
                animationProgress = fabAnimationProgress,
                toggleAnimation = { isMenuExtended.value = !isMenuExtended.value },
                onSpotifyClick = {
                    navController.navigate("spotify_screen") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                },
                onAboutClick = {
                    navController.navigate("home") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                },
                onProjectsClick = {
                    navController.navigate("projects_gallery") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                }
            )
        }
    }
}

@Composable
fun CvWithFluidNav(
    navController: NavController
) {
    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "fabProgress"
    )

    val clickAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 350,
            easing = LinearEasing
        ),
        label = "clickProgress"
    )

    val renderEffect: RenderEffect? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getBlurRenderEffect()
        } else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232633))
    ) {
        CvScreen(
            modifier = Modifier.let { base ->
                if (renderEffect != null && isMenuExtended.value) {
                    base.graphicsLayer { this.renderEffect = renderEffect }
                } else base
            }
        )

        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 14.dp)
        ) {
            CustomBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            Circle(
                color = MaterialTheme.colorScheme.onBackground,
                animationProgress = clickAnimationProgress
            )

            FabGroup(
                animationProgress = fabAnimationProgress,
                toggleAnimation = { isMenuExtended.value = !isMenuExtended.value },
                onSpotifyClick = {
                    navController.navigate("spotify_screen") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                },
                onAboutClick = {
                    navController.navigate("home") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                },
                onProjectsClick = {
                    navController.navigate("projects_gallery") {
                        launchSingleTop = true
                    }
                    isMenuExtended.value = false
                }
            )
        }
    }
}
