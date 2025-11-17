package com.example.aboutme.network

import com.squareup.moshi.Json

data class CurrentlyPlayingResponse(
    @Json(name = "is_playing") val isPlaying: Boolean?,
    @Json(name = "progress_ms") val progressMs: Int?,
    val item: SpotifyTrack?
)

data class SpotifyTrack(
    val name: String?,
    val artists: List<SpotifyArtist>?,
    @Json(name = "duration_ms") val durationMs: Int?
)

data class SpotifyArtist(
    val name: String?
)
