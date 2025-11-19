package com.example.aboutme.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SpotifyAuthState {
    var codeVerifier: String? = null
    var state: String? = null

    // Ahora son snapshot states
    var accessToken by mutableStateOf<String?>(null)
    var refreshToken by mutableStateOf<String?>(null)
    var expiresAt by mutableStateOf<Long?>(null)
}
