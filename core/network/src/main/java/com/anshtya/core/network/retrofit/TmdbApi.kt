package com.anshtya.core.network.retrofit

import com.anshtya.core.network.model.auth.DeleteSessionRequest
import com.anshtya.core.network.model.auth.LoginRequest
import com.anshtya.core.network.model.auth.LoginResponse
import com.anshtya.core.network.model.auth.NetworkAccountDetails
import com.anshtya.core.network.model.auth.RequestTokenResponse
import com.anshtya.core.network.model.auth.SessionRequest
import com.anshtya.core.network.model.auth.SessionResponse
import com.anshtya.core.network.model.content.ContentResponse
import com.anshtya.core.network.model.details.NetworkMovieDetails
import com.anshtya.core.network.model.details.NetworkPersonDetails
import com.anshtya.core.network.model.details.tv.NetworkTvDetails
import com.anshtya.core.network.model.library.FavoriteRequest
import com.anshtya.core.network.model.search.SearchResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("discover/{content_type}?include_video=false&with_watch_monetization_types=free&sort_by=popularity.desc")
    suspend fun getFreeContent(
        @Path("content_type") contentType: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String = "US",
        @Query("language") language: String = "en-US"
    ): ContentResponse

    @GET("discover/movie?include_video=false&with_watch_monetization_types=flatrate&sort_by=popularity.desc")
    suspend fun getPopularStreamingTitles(
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String = "US",
        @Query("language") language: String = "en-US"
    ): ContentResponse

    @GET("discover/movie?include_video=false&with_watch_monetization_types=rent&sort_by=popularity.desc")
    suspend fun getPopularTitlesOnRent(
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String = "US",
        @Query("language") language: String = "en-US"
    ): ContentResponse

    @GET("discover/movie?include_video=false&with_release_type=3|2&sort_by=popularity.desc")
    suspend fun getPopularTitlesInTheatres(
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String = "US",
        @Query("language") language: String = "en-US"
    ): ContentResponse

    @GET("trending/movie/{time_window}?language=en-US")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String,
        @Query("page") page: Int
    ): ContentResponse

    @GET("search/multi")
    suspend fun multiSearch(
        @Query("page") page: Int = 1,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean
    ): SearchResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int
    ): NetworkMovieDetails

    @GET("tv/{series_id}")
    suspend fun getTvShowDetails(
        @Path("series_id") id: Int
    ): NetworkTvDetails

    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") id: Int
    ): NetworkPersonDetails

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") accountId: Int
    ): ContentResponse

    @GET("account/{account_id}/favorite/tv")
    suspend fun getFavoriteTvShows(
        @Path("account_id") accountId: Int
    ): ContentResponse

    @Headers("content-type: application/json")
    @POST("account/{account_id}/favorite")
    suspend fun addOrRemoveFavorite(
        @Path("account_id") accountId: Int,
        @Body favoriteRequest: FavoriteRequest
    ): ContentResponse

    @GET("authentication/token/new")
    suspend fun createRequestToken(): RequestTokenResponse

    @Headers("content-type: application/json")
    @POST("authentication/token/validate_with_login")
    suspend fun validateWithLogin(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @Headers("content-type: application/json")
    @POST("authentication/session/new")
    suspend fun createSession(
        @Body sessionRequest: SessionRequest
    ): SessionResponse

    @GET("account")
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): NetworkAccountDetails

    @Headers("content-type: application/json")
    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(
        @Body deleteSessionRequest: DeleteSessionRequest
    )
}