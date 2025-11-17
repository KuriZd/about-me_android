package com.example.aboutme.auth

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

object PkceUtil {

    private const val CODE_VERIFIER_LENGTH = 64
    private const val ALLOWED_CHARS =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"

    fun generateCodeVerifier(): String {
        val random = SecureRandom()
        val sb = StringBuilder()
        repeat(CODE_VERIFIER_LENGTH) {
            val index = random.nextInt(ALLOWED_CHARS.length)
            sb.append(ALLOWED_CHARS[index])
        }
        return sb.toString()
    }

    fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(Charsets.US_ASCII)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        // Base64 URL-safe, sin padding
        return Base64.encodeToString(
            digest,
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
    }
}