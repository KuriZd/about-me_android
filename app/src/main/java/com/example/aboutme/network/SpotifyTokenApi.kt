package com.example.aboutme.network

import com.example.aboutme.auth.SPOTIFY_CLIENT_ID
import com.example.aboutme.auth.SPOTIFY_REDIRECT_URI
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SpotifyTokenApi {

    @FormUrlEncoded
    @POST("api/token")
    suspend fun exchangeCodeForToken(
        @Field("client_id") clientId: String = SPOTIFY_CLIENT_ID,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String = SPOTIFY_REDIRECT_URI,
        @Field("code_verifier") codeVerifier: String
    ): Response<ResponseBody>
}
