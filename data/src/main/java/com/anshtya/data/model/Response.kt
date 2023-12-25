package com.anshtya.data.model

sealed interface Response<out T> {
    data class Success<T>(val data: T): Response<T>
    data object Error: Response<Nothing>
}