package com.example.aboutme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale

@Composable
fun ProjectImagesScreen(
    navController: NavController,
    projectId: Int
) {
    val configuration = LocalConfiguration.current
    val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.locales[0]
    } else {
        @Suppress("DEPRECATION")
        configuration.locale
    }
    val isSpanish = locale.language == "es"

    val projectTitle: String
    val accent: Color

    when (projectId) {
        1 -> {
            projectTitle = "AboutMe Neon Portfolio"
            accent = Color(0xFF64FFDA)
        }
        2 -> {
            projectTitle = "Spotify Now Playing"
            accent = Color(0xFF7C4DFF)
        }
        3 -> {
            projectTitle = "RentIt Mobile"
            accent = Color(0xFFFF6B6B)
        }
        4 -> {
            projectTitle = "InnerGrowth"
            accent = Color(0xFFFFD54F)
        }
        5 -> {
            projectTitle = "MiTutor"
            accent = Color(0xFF42A5F5)
        }
        else -> {
            projectTitle = "Project"
            accent = MaterialTheme.colorScheme.primary
        }
    }

    val subtitle = if (isSpanish) {
        "Vista previa de algunas pantallas y secciones del proyecto."
    } else {
        "Preview of some screens and sections of the project."
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
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = projectTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = Color(0xFFB0BEC5)
                    )
                }
            }

            ImageBlock(
                title = if (isSpanish) "Pantalla principal" else "Main screen",
                accent = accent
            )

            ImageBlock(
                title = if (isSpanish) "Detalle o sección clave" else "Detail or key section",
                accent = accent
            )

            ImageBlock(
                title = if (isSpanish) "Vista complementaria" else "Complementary view",
                accent = accent
            )
        }
    }
}

@Composable
private fun ImageBlock(
    title: String,
    accent: Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            accent.copy(alpha = 0.35f),
                            Color(0xFF15172A),
                            Color(0xFF050816)
                        )
                    )
                )
        )
    }
}
