package com.anshtya.movieinfo.core.network.util

import com.anshtya.movieinfo.core.network.model.NetworkResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

private val json = Json { ignoreUnknownKeys = true }

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = apiCall()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Failure.HttpError(
                code = response.code(),
                errorMessage = parseErrorMessage(response.errorBody())
            )
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        NetworkResult.Failure.Unknown(e)
    }
}

private fun parseErrorMessage(errorBody: ResponseBody?): String {
    val message = errorBody?.let {
        runCatching {
            json.decodeFromString<ErrorResult>(it.string()).statusMessage
        }.getOrNull()
    }
    return message ?: "An unknown error occurred"
}

@Serializable
data class ErrorResult(
    @SerialName("status_code") val statusCode: Int? = null,
    @SerialName("status_message") val statusMessage: String? = null,
)
