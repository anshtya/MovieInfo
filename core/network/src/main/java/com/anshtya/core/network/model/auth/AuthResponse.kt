package com.anshtya.core.network.model.auth

import com.squareup.moshi.Json

data class RequestTokenResponse(
    @Json(name = "expires_at") val expiresAt: String,
    @Json(name = "request_token") val requestToken: String
)

data class LoginResponse(
    @Json(name = "request_token") val requestToken: String
)

data class SessionResponse(
    val success: Boolean,
    @Json(name = "session_id") val sessionId: String
)