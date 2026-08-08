package com.anshtya.movieinfo.core.network.util

import com.anshtya.movieinfo.core.network.model.NetworkResult
import com.anshtya.movieinfo.core.network.model.auth.LoginRequest
import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.core.network.retrofit.TmdbApi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.net.HttpURLConnection
import kotlin.coroutines.cancellation.CancellationException

class SafeApiCallTest {
    private lateinit var tmdbApi: TmdbApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        tmdbApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }

    @After
    fun tearDown() {
        runCatching { mockWebServer.close() }
    }

    @Test
    fun `returns success with parsed body when call succeeds`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setBody(resourceReader("/content.json"))
        )

        val result = safeApiCall { tmdbApi.getMovieLists(category = "", page = 1) }

        assertTrue(result is NetworkResult.Success)
        assertEquals(
            NetworkContentItem(
                id = 640146,
                title = "Ant-Man and the Wasp: Quantumania",
                name = null,
                posterPath = "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
            ),
            (result as NetworkResult.Success).data.results.first()
        )
    }

    @Test
    fun `returns http error with parsed message when body is a tmdb error`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .setBody("""{"status_code":401,"status_message":"error occurred"}""")
        )

        val result = safeApiCall { tmdbApi.validateWithLogin(LoginRequest("", "", "")) }

        assertTrue(result is NetworkResult.Failure.HttpError)
        val failure = result as NetworkResult.Failure.HttpError
        assertEquals(401, failure.code)
        assertEquals("error occurred", failure.errorMessage)
    }

    @Test
    fun `returns http error with fallback message when body is malformed`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                .setBody("not json")
        )

        val result = safeApiCall { tmdbApi.validateWithLogin(LoginRequest("", "", "")) }

        assertTrue(result is NetworkResult.Failure.HttpError)
        val failure = result as NetworkResult.Failure.HttpError
        assertEquals(500, failure.code)
        assertEquals("An unknown error occurred", failure.errorMessage)
    }

    @Test
    fun `returns http error with fallback message when body is empty`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )

        val result = safeApiCall { tmdbApi.validateWithLogin(LoginRequest("", "", "")) }

        assertTrue(result is NetworkResult.Failure.HttpError)
        assertEquals(
            "An unknown error occurred",
            (result as NetworkResult.Failure.HttpError).errorMessage
        )
    }

    @Test
    fun `returns unknown failure on connection error`() = runTest {
        mockWebServer.shutdown()

        val result = safeApiCall { tmdbApi.createRequestToken() }

        assertTrue(result is NetworkResult.Failure.Unknown)
        assertTrue((result as NetworkResult.Failure.Unknown).exception is IOException)
    }

    @Test
    fun `rethrows cancellation instead of wrapping it`() = runTest {
        var thrown: Throwable? = null

        try {
            safeApiCall<Unit> { throw CancellationException("cancelled") }
        } catch (e: CancellationException) {
            thrown = e
        }

        assertTrue(thrown is CancellationException)
    }
}

private fun resourceReader(filepath: String): String {
    return object {}.javaClass.getResource(filepath)!!.readText()
}
