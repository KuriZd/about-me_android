package com.example.aboutme.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SpotifyPlaybackService {

    private val api: SpotifyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(SpotifyApi::class.java)
    }

    suspend fun getCurrentlyPlaying(accessToken: String): CurrentlyPlayingResponse? {
        return withContext(Dispatchers.IO) {
            val response = api.getCurrentlyPlaying(
                authHeader = "Bearer $accessToken",
                market = "MX" // o null si no quieres filtrar por país
            )

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }
}
