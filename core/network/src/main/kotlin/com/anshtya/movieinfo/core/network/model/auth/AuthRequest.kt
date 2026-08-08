package com.anshtya.movieinfo.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    @SerialName("request_token") val requestToken: String
)

@Serializable
data class SessionRequest(
    @SerialName("request_token") val requestToken: String
)

@Serializable
data class DeleteSessionRequest(
    @SerialName("session_id") val sessionId: String
)