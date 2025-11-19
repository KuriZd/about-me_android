package com.example.aboutme.network

import android.util.Log
import com.example.aboutme.auth.SpotifyAuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SpotifyTokenService {

    private const val TAG = "SpotifyTokenService"

    private val api: SpotifyTokenApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(SpotifyTokenApi::class.java)
    }

    // ---------- Parseo común del JSON de tokens ----------
    private fun parseTokenJson(rawBody: String): TokenResponse? {
        if (rawBody.isBlank()) return null

        return try {
            val json = JSONObject(rawBody)

            val accessToken = json.optString("access_token", "")
            if (accessToken.isBlank()) {
                Log.e(TAG, "JSON sin access_token")
                null
            } else {
                val tokenType    = json.optString("token_type", "Bearer")
                val expiresIn    = json.optLong("expires_in", 0L)
                val refreshToken =
                    if (json.has("refresh_token")) json.optString("refresh_token", null)
                    else null
                val scope        =
                    if (json.has("scope")) json.optString("scope", null)
                    else null

                TokenResponse(
                    accessToken  = accessToken,
                    tokenType    = tokenType,
                    expiresIn    = expiresIn,
                    refreshToken = refreshToken,
                    scope        = scope
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parseando JSON de token", e)
            null
        }
    }

    // ---------- Intercambiar code (PKCE) por tokens ----------
    suspend fun exchangeCodeForTokens(code: String): TokenResponse? {
        val codeVerifier = SpotifyAuthState.codeVerifier
        if (codeVerifier == null) {
            Log.e(TAG, "codeVerifier es null, no se puede pedir token")
            return null
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = api.exchangeCodeForToken(
                    code = code,
                    codeVerifier = codeVerifier
                )

                val rawBody = response.body().orElseEmpty()

                Log.d(TAG, "token status = ${response.code()} msg = ${response.message()}")
                Log.d(TAG, "token raw body = $rawBody")

                if (!response.isSuccessful) {
                    null
                } else {
                    parseTokenJson(rawBody)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error solicitando token", e)
                null
            }
        }
    }

    // ---------- Refrescar access token usando refresh_token ----------
    suspend fun refreshAccessToken(refreshToken: String): TokenResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.refreshAccessToken(
                    refreshToken = refreshToken
                )

                val rawBody = response.body().orElseEmpty()

                Log.d(TAG, "refresh status = ${response.code()} msg = ${response.message()}")
                Log.d(TAG, "refresh raw body = $rawBody")

                if (!response.isSuccessful) {
                    null
                } else {
                    parseTokenJson(rawBody)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error refrescando token", e)
                null
            }
        }
    }

    // ---------- Extensión correcta ----------
    // Se define sobre ResponseBody? (NO sobre String)
    private fun ResponseBody?.orElseEmpty(): String =
        this?.string().orEmpty()
}
