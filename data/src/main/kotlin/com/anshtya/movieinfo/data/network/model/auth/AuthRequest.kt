package com.anshtya.movieinfo.data.network.model.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val username: String,
    val password: String,
    @Json(name = "request_token") val requestToken: String
)

@JsonClass(generateAdapter = true)
data class SessionRequest(
    @Json(name = "request_token") val requestToken: String
)

@JsonClass(generateAdapter = true)
data class DeleteSessionRequest(
    @Json(name = "session_id") val sessionId: String
)