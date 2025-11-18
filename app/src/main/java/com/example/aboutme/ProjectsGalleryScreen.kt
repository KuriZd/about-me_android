package com.example.aboutme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

data class GalleryProject(
    val id: Int,
    val title: String,
    val description: String,
    val tech: List<String>,
    val highlightColor: Color
)

@Composable
fun ProjectsGalleryScreen(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.locales[0]
    } else {
        @Suppress("DEPRECATION")
        configuration.locale
    }
    val isSpanish = locale.language == "es"
    val headerTitle = if (isSpanish) "Mis proyectos" else "My Projects"

    val projects = remember(isSpanish) {
        listOf(
            GalleryProject(
                id = 1,
                title = "AboutMe Neon Portfolio",
                description = if (isSpanish) {
                    "Pantalla principal animada con Jetpack Compose, blur dinámico y navegación fluida entre secciones personales."
                } else {
                    "Main animated screen built with Jetpack Compose, dynamic blur and smooth navigation between personal sections."
                },
                tech = listOf("Kotlin", "Compose", "Android"),
                highlightColor = Color(0xFF64FFDA)
            ),
            GalleryProject(
                id = 2,
                title = "Spotify Now Playing",
                description = if (isSpanish) {
                    "Integración con la API de Spotify para mostrar en tiempo real la canción actual, con UI adaptada al álbum."
                } else {
                    "Spotify API integration to show the currently playing track in real time, with album-aware UI."
                },
                tech = listOf("Kotlin", "Compose", "API REST"),
                highlightColor = Color(0xFF7C4DFF)
            ),
            GalleryProject(
                id = 3,
                title = "RentIt Mobile",
                description = if (isSpanish) {
                    "I'm currently working on this app. App de renta de artículos con Supabase, auth, carritos y diseño responsivo inspirado en marketplaces modernos."
                } else {
                    "I'm currently working on this app. Rental marketplace app using Supabase, auth, carts, and a responsive design inspired by modern marketplaces."
                },
                tech = listOf("React Native", "Supabase", "TypeScript", "Nativewind"),
                highlightColor = Color(0xFFFF6B6B)
            ),
            GalleryProject(
                id = 4,
                title = "InnerGrowth",
                description = if (isSpanish) {
                    "Aplicación de bienestar emocional con tracking de hábitos, estado de ánimo y pequeñas reflexiones diarias."
                } else {
                    "Emotional wellbeing app with habit tracking, mood logging, and short daily reflections."
                },
                tech = listOf("Node.js", "JS", "CSS", "SCSS", "HTML"),
                highlightColor = Color(0xFFFFD54F)
            ),
            GalleryProject(
                id = 5,
                title = "MiTutor",
                description = if (isSpanish) {
                    "Plataforma de gestión de tutorías académicas construida con Laravel 11, PHP y PostgreSQL, enfocada en el seguimiento de estudiantes."
                } else {
                    "Academic tutoring management platform built with Laravel 11, PHP, and PostgreSQL, focused on student tracking."
                },
                tech = listOf("Laravel 11", "PHP", "PostgreSQL", "Tailwind"),
                highlightColor = Color(0xFF42A5F5)
            )
        )
    }

    Box(
        modifier = modifier
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
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = headerTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            items(projects, key = { it.id }) { project ->
                ProjectCard(project = project)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ProjectCard(
    project: GalleryProject,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = project.highlightColor.copy(alpha = 0.6f),
                spotColor = project.highlightColor.copy(alpha = 0.9f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        project.highlightColor.copy(alpha = 0.35f),
                        Color(0xFF15172A),
                        Color(0xFF050816)
                    )
                )
            )
            .padding(1.5.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF050816))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = project.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                project.tech.forEach { tech ->
                    TechChip(
                        label = tech,
                        color = techColor(tech)
                    )
                }
            }

            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB0BEC5),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TechChip(
    label: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.12f))
            .borderGlow(color = color)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

@Composable
private fun Modifier.borderGlow(
    color: Color
): Modifier {
    return this
        .shadow(
            elevation = 12.dp,
            shape = CircleShape,
            ambientColor = color.copy(alpha = 0.7f),
            spotColor = color.copy(alpha = 0.9f),
            clip = false
        )
}

private fun techColor(tech: String): Color {
    return when (tech.lowercase()) {
        "kotlin" -> Color(0xFFFF6BFF)
        "compose" -> Color(0xFF64FFDA)
        "android" -> Color(0xFF69F0AE)
        "react native" -> Color(0xFF00E5FF)
        "supabase" -> Color(0xFF00C853)
        "typescript" -> Color(0xFF40C4FF)
        "firebase" -> Color(0xFFFFD54F)
        "api rest" -> Color(0xFFEF9A9A)
        "node.js" -> Color(0xFF00C853)
        "js" -> Color(0xFFFDD835)
        "css" -> Color(0xFF039BE5)
        "scss" -> Color(0xFFFF6BFF)
        "html" -> Color(0xFFFFA726)
        "laravel 11" -> Color(0xFFE53935)
        "php" -> Color(0xFF7E57C2)
        "postgresql" -> Color(0xFF1976D2)
        "tailwind" -> Color(0xFF38BDF8)
        "nativewind" -> Color(0xFF8E24AA)
        else -> Color(0xFFB0BEC5)
    }
}
