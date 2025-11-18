package com.example.aboutme.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyPlaybackApi {

    @GET("v1/me/player/currently-playing")
    suspend fun getCurrentlyPlaying(
        @Header("Authorization") authHeader: String
    ): Response<ResponseBody>

    @GET("v1/me/top/tracks")
    suspend fun getTopTracks(
        @Header("Authorization") authHeader: String,
        @Query("time_range") timeRange: String = "short_term",
        @Query("limit") limit: Int = 20
    ): Response<ResponseBody>
}
