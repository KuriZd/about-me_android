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

@Composable
fun AboutMeWithFluidNav(
    navController: NavController          // 👈 ahora lo recibimos por parámetro
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

        // Contenido principal (tu AboutMeScreen) con blur opcional
        AboutMeScreen(
            modifier = Modifier.let { base ->
                if (renderEffect != null && isMenuExtended.value) {
                    base.graphicsLayer { this.renderEffect = renderEffect }
                } else base
            }
        )

        // Nav + FABs + círculo de onda
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 14.dp)
        ) {
            // Bottom bar alineada al fondo
            CustomBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            // Onda de click (detrás de los FAB)
            Circle(
                color = MaterialTheme.colorScheme.onBackground,
                animationProgress = clickAnimationProgress
            )

            // Grupo de FABs, con el botón de Spotify navegando a otra vista
            FabGroup(
                animationProgress = fabAnimationProgress,
                toggleAnimation = { isMenuExtended.value = !isMenuExtended.value },
                onSpotifyClick = {
                    navController.navigate("spotify_screen")   // 👈 aquí ya compila
                }
            )
        }
    }
}
