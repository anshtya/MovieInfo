package com.anshtya.movieinfo.core.network

import com.anshtya.movieinfo.core.network.model.auth.ErrorResponse
import com.anshtya.movieinfo.core.network.model.auth.LoginRequest
import com.anshtya.movieinfo.core.network.model.auth.getErrorMessage
import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.core.network.retrofit.TmdbApi
import com.squareup.moshi.Moshi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class TmdbApiTest {
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

    @Test
    fun `test deserialization`() = runTest {
        val response = MockResponse()
            .setBody(resourceReader(this, "/content.json"))
        mockWebServer.enqueue(response)

        val content = tmdbApi.getMovieLists(category = "", page = 1)
        mockWebServer.takeRequest()

        assertEquals(
            NetworkContentItem(
                id = 640146,
                title = "Ant-Man and the Wasp: Quantumania",
                name = null,
                posterPath = "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
            ),
            content.results.first()
        )
    }

    @Test
    fun `test error deserialization`() = runTest {
        val errorMessage = ErrorResponse("error occurred")
        val moshiAdapter = Moshi.Builder()
            .build()
            .adapter(ErrorResponse::class.java)

        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
            .setBody(moshiAdapter.toJson(errorMessage))
        mockWebServer.enqueue(response)

        try {
            tmdbApi.validateWithLogin(LoginRequest("", "", ""))
            mockWebServer.takeRequest()
        } catch (e: HttpException) {
            assertEquals(
                errorMessage.statusMessage,
                getErrorMessage(e)
            )
        }
    }

    @After
    fun tearDown() {
        mockWebServer.close()
    }
}

private inline fun <reified T> resourceReader(caller: T, filepath: String): String {
    return caller!!::class.java.getResource(filepath)!!.readText()
}