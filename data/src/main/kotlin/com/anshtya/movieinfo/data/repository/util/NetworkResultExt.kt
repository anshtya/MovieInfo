package com.anshtya.movieinfo.data.repository.util

import com.anshtya.movieinfo.core.network.model.NetworkResult

internal inline fun <T, R> NetworkResult<T>.toResult(
    transform: (T) -> R
): Result<R> = when (this) {
    is NetworkResult.Success -> Result.success(transform(data))
    is NetworkResult.Failure.HttpError -> Result.failure(Exception(errorMessage))
    is NetworkResult.Failure.Unknown -> Result.failure(Exception("An error occurred", exception))
}
