package com.example.aboutme.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.util.UUID

object SpotifyAuthManager {

    fun startLogin(context: Context) {
        // 1. BORRAR tokens guardados
        val prefs = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        SpotifyAuthState.accessToken = null
        SpotifyAuthState.refreshToken = null

        // 2. PKCE
        val codeVerifier = PkceUtil.generateCodeVerifier()
        val codeChallenge = PkceUtil.generateCodeChallenge(codeVerifier)

        SpotifyAuthState.codeVerifier = codeVerifier
        SpotifyAuthState.state = UUID.randomUUID().toString()

        // 3. URL de autorización usando TU CONFIG
        val uri = Uri.parse(SPOTIFY_AUTH_URL).buildUpon()
            .appendQueryParameter("client_id", SPOTIFY_CLIENT_ID)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", SPOTIFY_REDIRECT_URI)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("scope", SPOTIFY_SCOPES.joinToString(" "))
            .appendQueryParameter("state", SpotifyAuthState.state)
            .build()

        Log.d("SpotifyAuth", "Auth URL = $uri") // para verificar que salga user-top-read

        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}
