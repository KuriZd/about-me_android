package com.example.aboutme.auth

const val SPOTIFY_CLIENT_ID = "89e02f14cb9d4163b6315b334bfc4a68"
const val SPOTIFY_REDIRECT_URI = "kuri-spotify-login://callback"

const val SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize"

val SPOTIFY_SCOPES = listOf(
    "user-read-currently-playing",
    "user-read-playback-state",
    "user-top-read"
)
