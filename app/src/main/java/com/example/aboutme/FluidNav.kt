package com.example.aboutme

// ui/FluidNav.kt

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect as AndroidRenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Flare

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Webhook
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aboutme.R
import kotlin.math.PI
import kotlin.math.sin

private const val DEFAULT_PADDING = 24

/* ---------------- Bottom Navigation "glass" ---------------- */

@Composable
fun CustomBottomNavigation(
    modifier: Modifier = Modifier
) {
    val blurEffect: RenderEffect? = remember { getBlurRenderEffect() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 1.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Aquí puedes reconstruir tu bottom bar usando blurEffect si lo necesitas
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val highlightColor = if (selected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    } else {
        Color.Transparent
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(highlightColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

/* ---------------- Grupo de FABs flotantes ---------------- */

@Composable
fun FabGroup(
    animationProgress: Float,
    toggleAnimation: () -> Unit,
    onSpotifyClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onProjectsClick: () -> Unit = {}
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // FAB izquierda -> Spotify (ahora neón)
        NeonImageFab(
            modifier = Modifier.padding(
                bottom = 72.dp * animationProgress,
                end = 210.dp * animationProgress
            ),
            painter = painterResource(id = R.drawable.ic_spotify),
            backgroundColor = Color(0xFF020B07),
            glowColor = Color(0xFF1DB954),
            onClick = onSpotifyClick
        )

        // FAB centro-arriba -> Home
        AnimatedFab(
            icon = Icons.Default.Home,
            modifier = Modifier.padding(
                bottom = 96.dp * animationProgress
            ),
            backgroundColor = Color(0xFF001B2E),
            glowColor = Color(0xFF00E5FF),
            onClick = onAboutClick
        )



        // FAB derecha -> Projects con neón morado
        AnimatedFab(
            icon = Icons.Default.Webhook,
            modifier = Modifier.padding(
                bottom = 72.dp * animationProgress,
                start = 210.dp * animationProgress
            ),
            backgroundColor = Color(0xFF0A061A),
            glowColor = Color(0xFF8E24AA),
            onClick = onProjectsClick
        )

        // FAB central con halo
        FabWithHalo(
            modifier = Modifier.offset(y = 6.dp),
            backgroundColor = MaterialTheme.colorScheme.secondary,
            animationProgress = animationProgress
        ) {
            AnimatedFab(
                icon = Icons.Default.Add,
                modifier = Modifier.rotate(225f * animationProgress),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                onClick = toggleAnimation
            )
        }

    }
}


@Composable
fun NeonImageFab(
    modifier: Modifier = Modifier,
    painter: Painter,
    backgroundColor: Color,
    glowColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    val hoverProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = tween(durationMillis = 180),
        label = "spotifyHover"
    )

    Box(
        modifier = modifier
            .size(72.dp)
            .graphicsLayer {
                val s = 1f + 0.1f * hoverProgress
                scaleX = s
                scaleY = s
            },
        contentAlignment = Alignment.Center
    ) {
        // Halo neón SIN RenderEffect
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    alpha = 0.6f + 0.4f * hoverProgress
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            glowColor.copy(alpha = 0.9f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        FloatingActionButton(
            onClick = onClick,
            interactionSource = interactionSource,
            containerColor = backgroundColor,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
                pressedElevation = 14.dp,
                focusedElevation = 10.dp,
                hoveredElevation = 12.dp
            )
        ) {
            Icon(
                painter = painter,
                contentDescription = "Spotify",
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}




@Composable
private fun FabWithHalo(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    animationProgress: Float,
    content: @Composable BoxScope.() -> Unit
) {
    val blurEffect: RenderEffect? = remember { getBlurRenderEffect() }
    val haloAlpha = 0.28f * animationProgress

    Box(
        modifier = modifier.size(83.dp),
        contentAlignment = Alignment.Center
    ) {
        // Halo difuso (no parece otro botón)
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    blurEffect?.let { renderEffect = it }
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            backgroundColor.copy(alpha = haloAlpha),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        content()
    }
}

@Composable
fun AnimatedFab(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    glowColor: Color? = null,
    onClick: () -> Unit = {}
) {
    val blurEffect: RenderEffect? = remember { getBlurRenderEffect() }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val hoverProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = tween(durationMillis = 180),
        label = "fabHover"
    )

    Box(
        modifier = modifier
            .size(64.dp)
            .graphicsLayer {
                val s = 1f + 0.08f * hoverProgress
                scaleX = s
                scaleY = s
            },
        contentAlignment = Alignment.Center
    ) {
        if (glowColor != null && blurEffect != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        renderEffect = blurEffect
                        alpha = 0.6f + 0.4f * hoverProgress
                    }
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.95f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        FloatingActionButton(
            onClick = onClick,
            interactionSource = interactionSource,
            containerColor = backgroundColor,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
                pressedElevation = 14.dp,
                focusedElevation = 10.dp,
                hoveredElevation = 12.dp
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}



/* ---------------- Círculo de “onda” al hacer click ---------------- */

@Composable
fun Circle(color: Color, animationProgress: Float) {
    val animationValue = sin(PI * animationProgress).toFloat()
    Box(
        modifier = Modifier
            .padding(DEFAULT_PADDING.dp)
            .size(56.dp)
            .scale(2 - animationValue)
            .border(
                width = 2.dp,
                color = color.copy(alpha = color.alpha * animationValue),
                shape = CircleShape
            )
    )
}

/* ---------------- Blur del fondo ---------------- */

fun getBlurRenderEffect(): RenderEffect? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        blurEffect().asComposeRenderEffect()
    } else null

@RequiresApi(Build.VERSION_CODES.S)
private fun blurEffect(): AndroidRenderEffect {
    val blur = AndroidRenderEffect.createBlurEffect(
        80f,
        80f,
        Shader.TileMode.MIRROR
    )

    val alpha = AndroidRenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, 50f, -5000f
                )
            )
        )
    )

    return AndroidRenderEffect.createChainEffect(alpha, blur)
}
