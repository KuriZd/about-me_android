// network/CurrentlyPlayingResponse
package com.example.aboutme.network

data class CurrentlyPlayingResponse(
    val progressMs: Int?,
    val item: SpotifyTrack?
)

data class SpotifyTrack(
    val name: String?,
    val artists: List<SpotifyArtist>?,
    val durationMs: Int?,
    val imageUrl: String?
)

data class SpotifyArtist(
    val name: String?
)
