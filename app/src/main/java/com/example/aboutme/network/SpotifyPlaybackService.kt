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
            .baseUrl("https://api.spotify.com/")
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

                val progressMs: Int? =
                    if (!json.isNull("progress_ms")) json.optInt("progress_ms") else null

                val itemJson = json.optJSONObject("item")
                if (itemJson == null) {
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

                Log.d(TAG, "progressMs=$progressMs durationMs=$durationMs imageUrl=$imageUrl title=$title")

                CurrentlyPlayingResponse(
                    progressMs = progressMs,
                    item = track
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error parseando playback", e)
                null
            }
        }
    }

    suspend fun getTopTracks(accessToken: String, limit: Int = 20): List<SpotifyTrack> {
        return withContext(Dispatchers.IO) {
            val ranges = listOf("short_term", "medium_term", "long_term")

            for (range in ranges) {
                try {
                    val response = api.getTopTracks(
                        authHeader = "Bearer $accessToken",
                        timeRange = range,
                        limit = limit
                    )

                    // Para respuestas 2xx body(), para errores errorBody()
                    val bodyStr = (response.body() ?: response.errorBody())
                        ?.string()
                        .orEmpty()

                    Log.d(TAG, "top tracks [$range] status=${response.code()} body=$bodyStr")

                    // Si no fue 2xx, probamos con el siguiente rango
                    if (!response.isSuccessful) {
                        continue
                    }

                    if (bodyStr.isBlank()) {
                        continue
                    }

                    val json = JSONObject(bodyStr)
                    val itemsArray = json.optJSONArray("items")
                        ?: continue

                    if (itemsArray.length() == 0) {
                        // Este rango no tiene datos, probamos con el siguiente
                        continue
                    }

                    val result = mutableListOf<SpotifyTrack>()

                    for (i in 0 until itemsArray.length()) {
                        val itemJson = itemsArray.optJSONObject(i) ?: continue

                        val title = itemJson.optString("name", null)

                        val durationMs: Int? =
                            if (!itemJson.isNull("duration_ms")) itemJson.optInt("duration_ms")
                            else null

                        val artistsArray = itemJson.optJSONArray("artists")
                        val artists = mutableListOf<SpotifyArtist>()
                        if (artistsArray != null) {
                            for (j in 0 until artistsArray.length()) {
                                val artistObj = artistsArray.optJSONObject(j)
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

                        result.add(
                            SpotifyTrack(
                                name = title,
                                artists = if (artists.isEmpty()) null else artists,
                                durationMs = durationMs,
                                imageUrl = imageUrl
                            )
                        )
                    }

                    Log.d(TAG, "top tracks parsed (range=$range) = ${result.size}")
                    // Si encontramos algún rango con datos, lo regresamos y ya
                    if (result.isNotEmpty()) return@withContext result
                } catch (e: Exception) {
                    Log.e(TAG, "Error parseando top tracks (range=$range)", e)
                }
            }

            Log.d(TAG, "No se encontraron top tracks en ningún rango")
            emptyList()
        }
    }

}
