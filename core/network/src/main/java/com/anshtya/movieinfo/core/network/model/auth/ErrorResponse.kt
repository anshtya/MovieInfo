package com.anshtya.movieinfo.core.network.model.auth

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException

data class ErrorResponse(
    @Json(name = "status_message") val statusMessage: String
)

fun getErrorMessage(e: HttpException): String? {
    val errorJson = e.response()?.errorBody()?.source()
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(ErrorResponse::class.java).lenient()
    return errorJson?.let { jsonAdapter.fromJson(it)?.statusMessage }
}