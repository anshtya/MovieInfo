package com.anshtya.core.network.model.auth

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody

data class ErrorResponse(
    @Json(name = "status_message") val statusMessage: String
)

fun ResponseBody?.getErrorMessage(): String? {
    val errorJson = this?.source()
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(ErrorResponse::class.java).lenient()
    return errorJson?.let { jsonAdapter.fromJson(it)?.statusMessage }
}