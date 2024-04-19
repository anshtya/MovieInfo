package com.anshtya.data.model

sealed class NetworkResponse<out T> {
    data class Success<T>(val data: T): NetworkResponse<T>()
    data class Error(
        val errorMessage: String? = "An error occurred"
    ): NetworkResponse<Nothing>()
}