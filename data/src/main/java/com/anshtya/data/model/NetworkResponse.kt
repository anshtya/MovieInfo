package com.anshtya.data.model

sealed interface NetworkResponse<out T> {
    data class Success<T>(val data: T): NetworkResponse<T>
    data class Error(val errorMessage: String? = null): NetworkResponse<Nothing>
}