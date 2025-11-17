package com.example.aboutme.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.util.UUID

object SpotifyAuthManager {

    fun startLogin(context: Context) {
        // 1) Generar PKCE
        val codeVerifier = PkceUtil.generateCodeVerifier()
        val codeChallenge = PkceUtil.generateCodeChallenge(codeVerifier)

        // 2) Guardar en estado global sencillo (para el ejemplo)
        SpotifyAuthState.codeVerifier = codeVerifier
        SpotifyAuthState.state = UUID.randomUUID().toString()

        // 3) Construir la URL de autorización
        val uri = Uri.parse(SPOTIFY_AUTH_URL).buildUpon()
            .appendQueryParameter("client_id", SPOTIFY_CLIENT_ID)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", SPOTIFY_REDIRECT_URI)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("scope", SPOTIFY_SCOPES.joinToString(" "))
            .appendQueryParameter("state", SpotifyAuthState.state)
            .build()

        // 4) Abrir navegador / app de Spotify
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}