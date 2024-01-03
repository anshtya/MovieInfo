package com.anshtya.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

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

data class ErrorResponse(
    @field:Json(name = "status_message") val statusMessage: String
)

fun ResponseBody?.getErrorMessage(): String? {
    val errorJson = this?.source()
    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(ErrorResponse::class.java).lenient()
    return errorJson?.let { jsonAdapter.fromJson(it)?.statusMessage }
}