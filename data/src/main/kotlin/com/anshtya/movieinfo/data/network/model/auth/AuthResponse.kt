package com.anshtya.movieinfo.data.network.model.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestTokenResponse(
    @Json(name = "expires_at") val expiresAt: String,
    @Json(name = "request_token") val requestToken: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "request_token") val requestToken: String
)

@JsonClass(generateAdapter = true)
data class SessionResponse(
    val success: Boolean,
    @Json(name = "session_id") val sessionId: String
)