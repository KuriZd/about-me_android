package com.example.aboutme.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApi {

    @GET("me/player/currently-playing")
    suspend fun getCurrentlyPlaying(
        @Header("Authorization") authHeader: String,
        @Query("market") market: String? = null // ej. "MX"
    ): Response<CurrentlyPlayingResponse>
}
