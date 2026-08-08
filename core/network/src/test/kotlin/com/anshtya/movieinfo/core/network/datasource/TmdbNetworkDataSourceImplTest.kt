package com.anshtya.movieinfo.core.network.datasource

import com.anshtya.movieinfo.core.network.model.NetworkResult
import com.anshtya.movieinfo.core.network.model.auth.DeleteSessionRequest
import com.anshtya.movieinfo.core.network.model.auth.LoginRequest
import com.anshtya.movieinfo.core.network.model.auth.SessionRequest
import com.anshtya.movieinfo.core.network.model.library.FavoriteRequest
import com.anshtya.movieinfo.core.network.model.library.WatchlistRequest
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
import java.net.HttpURLConnection

class TmdbNetworkDataSourceImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var dataSource: TmdbNetworkDataSource

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val tmdbApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
        dataSource = TmdbNetworkDataSourceImpl(tmdbApi)
    }

    @After
    fun tearDown() {
        runCatching { mockWebServer.close() }
    }

    @Test
    fun `getMovieLists hits the movie list endpoint`() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(emptyContentResponseJson))

        val result = dataSource.getMovieLists(category = "now_playing", page = 1)

        assertTrue(result is NetworkResult.Success)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/movie/now_playing"))
    }

    @Test
    fun `getTvShowLists hits the tv list endpoint`() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(emptyContentResponseJson))

        val result = dataSource.getTvShowLists(category = "popular", page = 1)

        assertTrue(result is NetworkResult.Success)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/tv/popular"))
    }

    @Test
    fun `multiSearch hits the search endpoint`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setBody("""{"page":1,"results":[],"total_pages":1}""")
        )

        val result = dataSource.multiSearch(query = "mario", includeAdult = false)

        assertTrue(result is NetworkResult.Success)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/search/multi"))
    }

    @Test
    fun `getMovieDetails hits the movie details endpoint and parses the body`() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(movieDetailsJson))

        val result = dataSource.getMovieDetails(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Test Movie", (result as NetworkResult.Success).data.title)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/movie/1"))
    }

    @Test
    fun `getTvShowDetails hits the tv details endpoint and parses the body`() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(tvDetailsJson))

        val result = dataSource.getTvShowDetails(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Test Show", (result as NetworkResult.Success).data.name)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/tv/1"))
    }

    @Test
    fun `getPersonDetails hits the person details endpoint and parses the body`() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(personDetailsJson))

        val result = dataSource.getPersonDetails(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Test Person", (result as NetworkResult.Success).data.name)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/person/1"))
    }

    @Test
    fun `getLibraryItems hits the account library endpoint`() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(emptyContentResponseJson))

        val result = dataSource.getLibraryItems(
            accountId = 1,
            itemType = "favorite",
            mediaType = "movies",
            page = 1
        )

        assertTrue(result is NetworkResult.Success)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/account/1/favorite/movies"))
    }

    @Test
    fun `addOrRemoveFavorite posts to the favorite endpoint`() = runTest {
        mockWebServer.enqueue(MockResponse())

        val result = dataSource.addOrRemoveFavorite(
            accountId = 1,
            favoriteRequest = FavoriteRequest(mediaType = "movie", mediaId = 1, favorite = true)
        )

        assertTrue(result is NetworkResult.Success)
        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertTrue(request.path!!.startsWith("/account/1/favorite"))
    }

    @Test
    fun `addOrRemoveFromWatchlist posts to the watchlist endpoint`() = runTest {
        mockWebServer.enqueue(MockResponse())

        val result = dataSource.addOrRemoveFromWatchlist(
            accountId = 1,
            watchlistRequest = WatchlistRequest(mediaType = "movie", mediaId = 1, watchlist = true)
        )

        assertTrue(result is NetworkResult.Success)
        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertTrue(request.path!!.startsWith("/account/1/watchlist"))
    }

    @Test
    fun `createRequestToken hits the request token endpoint`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setBody("""{"expires_at":"2024-01-01","request_token":"tok"}""")
        )

        val result = dataSource.createRequestToken()

        assertTrue(result is NetworkResult.Success)
        assertEquals("tok", (result as NetworkResult.Success).data.requestToken)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/authentication/token/new"))
    }

    @Test
    fun `validateWithLogin hits the login endpoint`() = runTest {
        mockWebServer.enqueue(MockResponse().setBody("""{"request_token":"tok"}"""))

        val result = dataSource.validateWithLogin(LoginRequest("user", "pass", "tok"))

        assertTrue(result is NetworkResult.Success)
        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertTrue(request.path!!.startsWith("/authentication/token/validate_with_login"))
    }

    @Test
    fun `createSession hits the session endpoint`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setBody("""{"success":true,"session_id":"sess"}""")
        )

        val result = dataSource.createSession(SessionRequest("tok"))

        assertTrue(result is NetworkResult.Success)
        assertEquals("sess", (result as NetworkResult.Success).data.sessionId)
        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertTrue(request.path!!.startsWith("/authentication/session/new"))
    }

    @Test
    fun `getAccountDetails hits the account endpoint with session id`() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(accountDetailsJson))

        val result = dataSource.getAccountDetails(sessionId = "sess")

        assertTrue(result is NetworkResult.Success)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/account?session_id=sess"))
    }

    @Test
    fun `getAccountDetailsWithId hits the account by id endpoint`() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(accountDetailsJson))

        val result = dataSource.getAccountDetailsWithId(accountId = 1)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Test User", (result as NetworkResult.Success).data.name)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.startsWith("/account/1"))
    }

    @Test
    fun `deleteSession hits the session endpoint with DELETE`() = runTest {
        mockWebServer.enqueue(MockResponse())

        val result = dataSource.deleteSession(DeleteSessionRequest(sessionId = "sess"))

        assertTrue(result is NetworkResult.Success)
        val request = mockWebServer.takeRequest()
        assertEquals("DELETE", request.method)
        assertTrue(request.path!!.startsWith("/authentication/session"))
    }

    @Test
    fun `failure from the api surfaces as NetworkResult Failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody("""{"status_code":404,"status_message":"not found"}""")
        )

        val result = dataSource.getMovieDetails(1)

        assertTrue(result is NetworkResult.Failure.HttpError)
        assertEquals("not found", (result as NetworkResult.Failure.HttpError).errorMessage)
    }
}

private val emptyContentResponseJson = """
    {"page":1,"results":[],"total_pages":1,"total_results":0}
""".trimIndent()

private val movieDetailsJson = """
    {
      "adult": false,
      "backdrop_path": null,
      "budget": 0,
      "credits": {"cast": [], "crew": []},
      "genres": [],
      "id": 1,
      "original_language": "en",
      "overview": "",
      "popularity": 0.0,
      "poster_path": null,
      "production_companies": [],
      "production_countries": [],
      "recommendations": {"page":1,"results":[],"total_pages":1,"total_results":0},
      "release_date": "2024-01-01",
      "revenue": 0,
      "runtime": 0,
      "tagline": "",
      "title": "Test Movie",
      "vote_average": 0.0,
      "vote_count": 0
    }
""".trimIndent()

private val tvDetailsJson = """
    {
      "adult": false,
      "backdrop_path": null,
      "created_by": [],
      "credits": {"cast": [], "crew": []},
      "episode_run_time": [],
      "first_air_date": "2024-01-01",
      "genres": [],
      "id": 1,
      "in_production": false,
      "last_air_date": "2024-01-01",
      "last_episode_to_air": {
        "air_date": "2024-01-01",
        "episode_number": 1,
        "id": 1,
        "name": "Pilot",
        "overview": "",
        "production_code": "",
        "runtime": 30,
        "season_number": 1,
        "show_id": 1,
        "still_path": null,
        "vote_average": 0.0,
        "vote_count": 0
      },
      "name": "Test Show",
      "networks": [],
      "next_episode_to_air": null,
      "number_of_episodes": 1,
      "number_of_seasons": 1,
      "origin_country": [],
      "original_language": "en",
      "overview": "",
      "poster_path": null,
      "production_companies": [],
      "production_countries": [],
      "recommendations": {"page":1,"results":[],"total_pages":1,"total_results":0},
      "status": "Ended",
      "tagline": "",
      "type": "Scripted",
      "vote_average": 0.0,
      "vote_count": 0
    }
""".trimIndent()

private val personDetailsJson = """
    {
      "adult": false,
      "also_known_as": [],
      "biography": null,
      "birthday": null,
      "deathday": null,
      "gender": 0,
      "id": 1,
      "known_for_department": "Acting",
      "name": "Test Person",
      "place_of_birth": null,
      "profile_path": null
    }
""".trimIndent()

private val accountDetailsJson = """
    {
      "avatar": {
        "gravatar": {"hash": "abc"},
        "tmdb": {"avatar_path": null}
      },
      "id": 1,
      "include_adult": false,
      "iso_639_1": "en",
      "iso_3166_1": "US",
      "name": "Test User",
      "username": "testuser"
    }
""".trimIndent()
