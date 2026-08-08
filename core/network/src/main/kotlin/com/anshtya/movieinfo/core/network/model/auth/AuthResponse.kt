package com.anshtya.movieinfo.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestTokenResponse(
    @SerialName("expires_at") val expiresAt: String,
    @SerialName("request_token") val requestToken: String
)

@Serializable
data class LoginResponse(
    @SerialName("request_token") val requestToken: String
)

@Serializable
data class SessionResponse(
    val success: Boolean,
    @SerialName("session_id") val sessionId: String
)