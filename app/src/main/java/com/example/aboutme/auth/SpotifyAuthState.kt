package com.example.aboutme.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SpotifyAuthState {
    var codeVerifier: String? = null
    var state: String? = null

    var accessToken: String? by mutableStateOf(null)

    var refreshToken: String? = null
}
