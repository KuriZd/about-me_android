package com.example.aboutme.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SpotifyPlaybackService {

    private const val TAG = "SpotifyPlaybackService"

    private val api: SpotifyPlaybackApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.spotify.com/") // importante la barra final
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(SpotifyPlaybackApi::class.java)
    }

    suspend fun getCurrentlyPlaying(accessToken: String): CurrentlyPlayingResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCurrentlyPlaying("Bearer $accessToken")

                val rawBody = response.body()?.string().orEmpty()
                Log.d(TAG, "playback status=${response.code()} body=$rawBody")

                if (!response.isSuccessful || rawBody.isBlank()) {
                    return@withContext null
                }

                val json = JSONObject(rawBody)

                // progress_ms va en el nivel raíz
                val progressMs: Int? =
                    if (!json.isNull("progress_ms")) json.optInt("progress_ms") else null

                // item es el track actual
                val itemJson = json.optJSONObject("item")
                if (itemJson == null) {
                    Log.d(TAG, "JSON sin item (no hay canción?)")
                    return@withContext CurrentlyPlayingResponse(progressMs, null)
                }

                val title = itemJson.optString("name", null)

                val durationMs: Int? =
                    if (!itemJson.isNull("duration_ms")) itemJson.optInt("duration_ms") else null

                val artistsArray = itemJson.optJSONArray("artists")
                val artists = mutableListOf<SpotifyArtist>()
                if (artistsArray != null) {
                    for (i in 0 until artistsArray.length()) {
                        val artistObj = artistsArray.optJSONObject(i)
                        val artistName = artistObj?.optString("name", null)
                        if (!artistName.isNullOrBlank()) {
                            artists.add(SpotifyArtist(name = artistName))
                        }
                    }
                }

                val albumJson = itemJson.optJSONObject("album")
                val imagesArray = albumJson?.optJSONArray("images")
                var imageUrl: String? = null
                if (imagesArray != null && imagesArray.length() > 0) {
                    val imgObj = imagesArray.optJSONObject(0)
                    imageUrl = imgObj?.optString("url", null)
                }

                val track = SpotifyTrack(
                    name = title,
                    artists = if (artists.isEmpty()) null else artists,
                    durationMs = durationMs,
                    imageUrl = imageUrl
                )

                Log.d(
                    TAG,
                    "progressMs=$progressMs durationMs=$durationMs imageUrl=$imageUrl title=$title"
                )

                return@withContext CurrentlyPlayingResponse(
                    progressMs = progressMs,
                    item = track
                )

            } catch (e: Exception) {
                Log.e(TAG, "Error parseando playback", e)
                null
            }
        }
    }
}
