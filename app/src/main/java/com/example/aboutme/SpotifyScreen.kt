package com.example.aboutme.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aboutme.R
import com.example.aboutme.auth.SpotifyAuthManager
import com.example.aboutme.auth.SpotifyAuthState
import com.example.aboutme.network.SpotifyPlaybackService

data class TrackUi(
    val title: String,
    val artist: String,
    val duration: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // 🔑 clave para forzar recargas manuales y al arrancar
    var refreshKey by remember { mutableStateOf(0) }

    // Cargar token guardado al entrar
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences(
            "spotify_prefs",
            android.content.Context.MODE_PRIVATE
        )
        val savedToken = prefs.getString("access_token", null)
        Log.d("SpotifyScreen", "savedToken from prefs = ${savedToken?.take(10) ?: "null"}")

        if (savedToken != null && SpotifyAuthState.accessToken == null) {
            SpotifyAuthState.accessToken = savedToken
            // 👇 forzamos una recarga de "Reproduciendo ahora"
            refreshKey++
        }
    }

    val accessToken = SpotifyAuthState.accessToken

    LaunchedEffect(accessToken) {
        Log.d("SpotifyScreen", "accessToken = ${accessToken?.take(10) ?: "null"}")
    }

    // Estado para "Reproduciendo ahora"
    var nowPlaying by remember { mutableStateOf<TrackUi?>(null) }
    var nowPlayingLoading by remember { mutableStateOf(false) }
    var nowPlayingError by remember { mutableStateOf<String?>(null) }

    // 👇 Usa también refreshKey aquí
    LaunchedEffect(accessToken, refreshKey) {
        if (accessToken == null) {
            nowPlaying = null
            nowPlayingError = null
            nowPlayingLoading = false
            return@LaunchedEffect
        }

        nowPlayingLoading = true
        nowPlayingError = null

        try {
            val response = SpotifyPlaybackService.getCurrentlyPlaying(accessToken)
            val track = response?.item

            if (track != null && track.name != null) {
                val title = track.name
                val artist = track.artists?.firstOrNull()?.name ?: "Unknown"
                val durationMs = track.durationMs ?: 0

                val totalSeconds = durationMs / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                val durationText = "%d:%02d".format(minutes, seconds)

                nowPlaying = TrackUi(
                    title = title,
                    artist = artist,
                    duration = durationText
                )
            } else {
                nowPlaying = null
                nowPlayingError = "No hay nada reproduciéndose"
            }
        } catch (e: Exception) {
            nowPlayingError = "Error al obtener la reproducción"
            nowPlaying = null
        } finally {
            nowPlayingLoading = false
        }
    }

    val fakeTracks = listOf(
        TrackUi("Focus mode", "Lo-fi beats", "2:31"),
        TrackUi("Clean deploy", "Dev Flow", "3:12"),
        TrackUi("Night coding", "Synthwave", "4:03"),
        TrackUi("Bug fixing", "Soft electronica", "3:44"),
        TrackUi("Refactor vibes", "Indie chill", "2:57")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Spotify", fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        containerColor = Color(0xFF05080D)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1DB954).copy(alpha = 0.20f),
                            Color(0xFF05080D)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Header con logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF121212)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_spotify),
                            contentDescription = "Playlist cover",
                            modifier = Modifier
                                .size(84.dp)
                                .padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Coding playlist",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Para concentrarte mientras codeas",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para iniciar login con Spotify o estado conectado
                if (accessToken == null) {
                    Button(
                        onClick = { SpotifyAuthManager.startLogin(context) }
                    ) {
                        Text(text = "Conectar con Spotify")
                    }
                } else {
                    Text(
                        text = "Conectado a Spotify ✅",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF1DB954)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bloque "Reproduciendo ahora" mejorado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reproduciendo ahora",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (accessToken != null) {
                                refreshKey++ // fuerza recarga
                            }
                        },
                        enabled = accessToken != null && !nowPlayingLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = if (accessToken != null)
                                Color.White.copy(alpha = 0.8f)
                            else
                                Color.White.copy(alpha = 0.3f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                when {
                    accessToken == null -> {
                        Text(
                            text = "Conéctate a Spotify para ver tu reproducción actual",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    nowPlayingLoading -> {
                        NowPlayingSkeletonCard()
                    }
                    nowPlayingError != null -> {
                        Text(
                            text = nowPlayingError ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    nowPlaying != null -> {
                        NowPlayingCard(track = nowPlaying!!)
                    }
                    else -> {
                        Text(
                            text = "No hay nada reproduciéndose",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Tracks",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(fakeTracks) { track ->
                        TrackRow(track = track)
                    }
                }
            }
        }
    }
}

@Composable
private fun NowPlayingCard(track: TrackUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // “Portada” circular con icono de play
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF1DB954),
                                Color(0xFF121212)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Reproduciendo",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = track.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Text(
                text = track.duration,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun NowPlayingSkeletonCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121212)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .fillMaxWidth(0.7f)
                        .background(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth(0.4f)
                        .background(
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun TrackRow(
    track: TrackUi
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        Text(
            text = track.duration,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}
