package com.anshtya.movieinfo.core.network.model.auth

import com.squareup.moshi.Json

data class LoginRequest(
    val username: String,
    val password: String,
    @Json(name = "request_token") val requestToken: String
)

data class SessionRequest(
    @Json(name = "request_token") val requestToken: String
)

data class DeleteSessionRequest(
    @Json(name = "session_id") val sessionId: String
)