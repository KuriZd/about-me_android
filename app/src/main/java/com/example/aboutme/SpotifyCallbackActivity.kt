package com.example.aboutme

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.auth.SpotifyAuthState
import com.example.aboutme.network.SpotifyTokenService
import kotlinx.coroutines.launch

class SpotifyCallbackActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent?.data
        val code = data?.getQueryParameter("code")
        val state = data?.getQueryParameter("state")
        val error = data?.getQueryParameter("error")

        if (error != null) {
            Toast.makeText(this, "Error de Spotify: $error", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val expectedState = SpotifyAuthState.state
        if (expectedState != null && state != expectedState) {
            Toast.makeText(this, "State inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (code == null) {
            Toast.makeText(this, "No se recibió código de autorización", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            val tokens = SpotifyTokenService.exchangeCodeForTokens(code)
            if (tokens != null) {
                // 1) Guardar en estado global observable
                SpotifyAuthState.accessToken = tokens.accessToken
                SpotifyAuthState.refreshToken = tokens.refreshToken

                // 2) Guardar en SharedPreferences
                val prefs = getSharedPreferences("spotify_prefs", MODE_PRIVATE)
                prefs.edit()
                    .putString("access_token", tokens.accessToken)
                    .putString("refresh_token", tokens.refreshToken)
                    .apply()

                Toast.makeText(
                    this@SpotifyCallbackActivity,
                    "Login con Spotify exitoso",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@SpotifyCallbackActivity,
                    "No se pudo obtener el token",
                    Toast.LENGTH_SHORT
                ).show()
            }

            finish()
        }
    }
}
