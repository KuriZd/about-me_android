package com.example.aboutme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale

data class ProjectImageTile(
    val id: Int,
    val titleEs: String,
    val titleEn: String
)

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
    val isSpanish = locale.language.startsWith("es")

    // --- Datos del proyecto (titulo, color, descripción y tecnologías) ---
    val projectTitle: String
    val accent: Color
    val descriptionEs: String
    val descriptionEn: String
    val techStack: List<String>

    when (projectId) {
        1 -> {
            projectTitle = "AboutMe Neon Portfolio"
            accent = Color(0xFF64FFDA)
            descriptionEs =
                "Pantalla principal animada con Jetpack Compose, blur dinámico y navegación fluida entre secciones personales."
            descriptionEn =
                "Main animated screen built with Jetpack Compose, dynamic blur and fluid navigation between personal sections."
            techStack = listOf("Kotlin", "Compose", "Android")
        }
        2 -> {
            projectTitle = "Spotify Now Playing"
            accent = Color(0xFF7C4DFF)
            descriptionEs =
                "Integración con la API de Spotify para mostrar en tiempo real la canción actual, con UI adaptada al álbum."
            descriptionEn =
                "Spotify API integration to show the currently playing track in real time, with album-aware UI."
            techStack = listOf("Kotlin", "Compose", "API REST")
        }
        3 -> {
            projectTitle = "RentIt Mobile"
            accent = Color(0xFFFF6B6B)
            descriptionEs =
                "App de renta de artículos con Supabase, autenticación, carritos y diseño responsivo inspirado en marketplaces modernos."
            descriptionEn =
                "Rental marketplace app with Supabase, auth, carts and a responsive design inspired by modern marketplaces."
            techStack = listOf("React Native", "NativeWind", "Supabase", "TypeScript")
        }
        4 -> {
            projectTitle = "InnerGrowth"
            accent = Color(0xFFFFD54F)
            descriptionEs =
                "Aplicación de bienestar emocional con tracking de hábitos, estado de ánimo y pequeñas reflexiones diarias."
            descriptionEn =
                "Emotional-wellbeing app with habit tracking, mood logging and small daily reflections."
            techStack = listOf("Node.js", "SCSS", "HTML", "JS", "CSS")
        }
        5 -> {
            projectTitle = "MiTutor"
            accent = Color(0xFF42A5F5)
            descriptionEs =
                "Plataforma para gestión de tutorías académicas con panel para tutores, alumnos y reportes de progreso."
            descriptionEn =
                "Platform for managing academic tutoring with dashboards for tutors, students and progress reports."
            techStack = listOf("Laravel 11", "PHP", "PostgreSQL", "Tailwind")
        }
        else -> {
            projectTitle = "Project"
            accent = MaterialTheme.colorScheme.primary
            descriptionEs = "Vista previa del proyecto."
            descriptionEn = "Project preview."
            techStack = emptyList()
        }
    }

    val subtitle = if (isSpanish) {
        "Vista previa de algunas pantallas y secciones del proyecto."
    } else {
        "Preview of some screens and sections of the project."
    }

    val aboutLabel = if (isSpanish) "Sobre este proyecto" else "About this project"
    val techLabel = if (isSpanish) "Tecnologías usadas" else "Tech stack"
    val screensLabel = if (isSpanish) "Screens" else "Screens"

    val tiles = remember(projectId, isSpanish) {
        listOf(
            ProjectImageTile(1, "Pantalla principal", "Main screen"),
            ProjectImageTile(2, "Detalle de contenido", "Content detail"),
            ProjectImageTile(3, "Lista / feed", "List / feed"),
            ProjectImageTile(4, "Pantalla de búsqueda", "Search screen"),
            ProjectImageTile(5, "Onboarding o login", "Onboarding or login"),
            ProjectImageTile(6, "Vista complementaria", "Complementary view")
        )
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
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = projectTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = Color(0xFFB0BEC5),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Descripción
            Text(
                text = aboutLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = accent
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isSpanish) descriptionEs else descriptionEn,
                fontSize = 12.sp,
                color = Color(0xFFE0E0E0),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tech stack chips
            if (techStack.isNotEmpty()) {
                Text(
                    text = techLabel,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFB0BEC5)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    techStack.forEach { tech ->
                        TechChipForImages(
                            label = tech,
                            accent = accent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título de la grilla
            Text(
                text = screensLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = accent
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Grid de imágenes
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 32.dp, top = 4.dp)
            ) {
                items(tiles, key = { it.id }) { tile ->
                    ProjectImageCard(
                        title = if (isSpanish) tile.titleEs else tile.titleEn,
                        accent = accent
                    )
                }
            }
        }
    }
}

@Composable
private fun TechChipForImages(
    label: String,
    accent: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(accent.copy(alpha = 0.18f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = accent.copy(alpha = 0.95f)
        )
    }
}

@Composable
private fun ProjectImageCard(
    title: String,
    accent: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(26.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        accent.copy(alpha = 0.7f),
                        accent.copy(alpha = 0.1f),
                        Color(0xFF050816)
                    )
                )
            )
            .padding(1.5.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF050816))
            .aspectRatio(0.58f)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.10f),
                            Color.Transparent,
                            Color.White.copy(alpha = 0.06f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black.copy(alpha = 0.25f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "2023 STYLE IS.",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
