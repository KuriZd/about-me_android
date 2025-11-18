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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.aboutme.R
import com.example.aboutme.auth.SpotifyAuthManager
import com.example.aboutme.auth.SpotifyAuthState
import com.example.aboutme.network.SpotifyPlaybackService
import kotlinx.coroutines.delay

data class TrackUi(
    val title: String,
    val artist: String,
    val duration: String,
    val imageUrl: String?
)


data class NowPlayingUi(
    val title: String,
    val artist: String,
    val position: String,
    val duration: String,
    val imageUrl: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences(
            "spotify_prefs",
            android.content.Context.MODE_PRIVATE
        )
        val savedToken = prefs.getString("access_token", null)
        Log.d("SpotifyScreen", "savedToken = ${savedToken?.take(10) ?: "null"}")
        if (savedToken != null && SpotifyAuthState.accessToken == null) {
            SpotifyAuthState.accessToken = savedToken
        }
    }

    val accessToken = SpotifyAuthState.accessToken

    LaunchedEffect(accessToken) {
        Log.d("SpotifyScreen", "accessToken = ${accessToken?.take(10) ?: "null"}")
    }

    var nowPlaying by remember { mutableStateOf<NowPlayingUi?>(null) }
    var nowPlayingLoading by remember { mutableStateOf(false) }
    var nowPlayingError by remember { mutableStateOf<String?>(null) }

    var topTracks by remember { mutableStateOf<List<TrackUi>>(emptyList()) }
    var topTracksLoading by remember { mutableStateOf(false) }
    var topTracksError by remember { mutableStateOf<String?>(null) }

    suspend fun loadNowPlaying(accessTokenNonNull: String) {
        nowPlayingLoading = true
        nowPlayingError = null

        try {
            val response = SpotifyPlaybackService.getCurrentlyPlaying(accessTokenNonNull)
            val track = response?.item

            if (track != null && track.name != null) {
                val title = track.name
                val artist = track.artists?.firstOrNull()?.name ?: "Unknown"
                val durationMs = track.durationMs ?: 0
                val progressMs = response.progressMs ?: 0
                val imageUrl = track.imageUrl

                fun format(ms: Int): String {
                    val totalSeconds = ms / 1000
                    val minutes = totalSeconds / 60
                    val seconds = totalSeconds % 60
                    return "%d:%02d".format(minutes, seconds)
                }
                Log.d(
                    "SpotifyPlaybackService",
                    "progressMs=${response.progressMs}, durationMs=${track.durationMs}, imageUrl=$imageUrl"
                )

                nowPlaying = NowPlayingUi(
                    title = title,
                    artist = artist,
                    position = format(progressMs),
                    duration = format(durationMs),
                    imageUrl = imageUrl
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

    suspend fun loadTopTracks(accessTokenNonNull: String) {
        topTracksLoading = true
        topTracksError = null

        try {
            val apiTracks = SpotifyPlaybackService.getTopTracks(accessTokenNonNull, limit = 20)

            fun format(ms: Int?): String {
                val value = ms ?: 0
                val totalSeconds = value / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                return "%d:%02d".format(minutes, seconds)
            }

            topTracks = apiTracks.map { t ->
                TrackUi(
                    title = t.name ?: "Sin título",
                    artist = t.artists?.firstOrNull()?.name ?: "Unknown",
                    duration = format(t.durationMs),
                    imageUrl = t.imageUrl
                )
            }

        } catch (e: Exception) {
            topTracksError = "No se pudieron cargar tus temas más escuchados"
            topTracks = emptyList()
        } finally {
            topTracksLoading = false
        }
    }

    LaunchedEffect(accessToken) {
        if (accessToken == null) {
            nowPlaying = null
            nowPlayingError = null
            nowPlayingLoading = false
            return@LaunchedEffect
        }

        while (true) {
            loadNowPlaying(accessToken)
            delay(8_000L)
        }
    }

    LaunchedEffect(accessToken) {
        if (accessToken == null) {
            topTracks = emptyList()
            topTracksLoading = false
            topTracksError = null
            return@LaunchedEffect
        }

        loadTopTracks(accessToken)
    }

    val fakeTracks = listOf(
        TrackUi("Focus mode", "Lo-fi beats", "2:31", null),
        TrackUi("Clean deploy", "Dev Flow", "3:12", null),
        TrackUi("Night coding", "Synthwave", "4:03", null),
        TrackUi("Bug fixing", "Soft electronica", "3:44", null),
        TrackUi("Refactor vibes", "Indie chill", "2:57", null)
    )


    Scaffold(
        containerColor = Color(0xFF05080D),
        modifier = modifier
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (nowPlaying?.imageUrl != null)
                                Color.Transparent
                            else
                                Color(0xFF121212)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        if (nowPlaying?.imageUrl != null) {
                            AsyncImage(
                                model = nowPlaying!!.imageUrl,
                                contentDescription = "Portada actual",
                                modifier = Modifier
                                    .size(84.dp)
                                    .clip(RoundedCornerShape(18.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_spotify),
                                contentDescription = "Playlist cover",
                                modifier = Modifier
                                    .size(84.dp)
                                    .padding(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Coding Time",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Para concentrarte mientras codeas",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (accessToken == null) {
                    Button(
                        onClick = { SpotifyAuthManager.startLogin(context) }
                    ) {
                        Text(text = "Conectar con Spotify")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Reproduciendo ahora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                when {
                    accessToken == null -> {
                        Text(
                            text = "Conéctate a Spotify para ver tu reproducción actual",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    nowPlayingLoading && nowPlaying == null -> {
                        NowPlayingSkeletonCard()
                    }
                    nowPlayingError != null && nowPlaying == null -> {
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
                    text = if (accessToken != null) "Tus más escuchados del último mes" else "Tracks",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                if (accessToken != null && topTracksLoading && topTracks.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cargando tus temas más escuchados...",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                } else if (accessToken != null && topTracksError != null && topTracks.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = topTracksError ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val tracksToShow = when {
                    accessToken != null && topTracks.isNotEmpty() -> topTracks
                    else -> fakeTracks
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(tracksToShow) { track ->
                        TrackRow(track = track)
                    }
                }
            }
        }
    }
}

@Composable
private fun NowPlayingCard(track: NowPlayingUi) {
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
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (track.imageUrl != null) {
                    AsyncImage(
                        model = track.imageUrl,
                        contentDescription = "Portada del álbum",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.25f))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF1DB954),
                                        Color(0xFF121212)
                                    )
                                )
                            )
                    )
                }

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
                text = "${track.position} / ${track.duration}",
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
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (track.imageUrl != null) {
                AsyncImage(
                    model = track.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color(0xFF121212)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_spotify),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

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

