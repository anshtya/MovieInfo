package com.anshtya.core.network.model

import com.squareup.moshi.Json

data class LoginRequest(
    val username: String,
    val password: String,
    @field:Json(name = "request_token") val requestToken: String
)

data class SessionRequest(
    @field:Json(name = "request_token") val requestToken: String
)

data class DeleteSessionRequest(
    @field:Json(name = "session_id") val sessionId: String
)