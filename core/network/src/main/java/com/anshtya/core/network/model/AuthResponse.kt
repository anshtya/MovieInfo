package com.anshtya.core.network.model

import com.squareup.moshi.Json

data class RequestTokenResponse(
    @field:Json(name = "expires_at") val expiresAt: String,
    @field:Json(name = "request_token") val requestToken: String
)

data class LoginResponse(
    @field:Json(name = "request_token") val requestToken: String
)

data class SessionResponse(
    val success: Boolean,
    @field:Json(name = "session_id") val sessionId: String
)