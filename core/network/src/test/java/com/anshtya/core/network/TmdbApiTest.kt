package com.anshtya.core.network

import com.anshtya.core.network.model.content.NetworkContentItem
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.core.testing.rules.MainDispatcherRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TmdbApiTest {
    private lateinit var tmdbApi: TmdbApi
    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        tmdbApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
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

    @After
    fun tearDown() {
        mockWebServer.close()
    }
}

private inline fun <reified T> resourceReader(caller: T, filepath: String): String {
    return caller!!::class.java.getResource(filepath)!!.readText()
}