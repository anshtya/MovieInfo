package com.anshtya.core.network.retrofit

import com.anshtya.core.network.model.auth.DeleteSessionRequest
import com.anshtya.core.network.model.auth.LoginRequest
import com.anshtya.core.network.model.auth.LoginResponse
import com.anshtya.core.network.model.auth.NetworkAccountDetails
import com.anshtya.core.network.model.auth.RequestTokenResponse
import com.anshtya.core.network.model.auth.SessionRequest
import com.anshtya.core.network.model.auth.SessionResponse
import com.anshtya.core.network.model.content.NetworkContentResponse
import com.anshtya.core.network.model.details.NetworkMovieDetails
import com.anshtya.core.network.model.details.people.NetworkPersonDetails
import com.anshtya.core.network.model.details.tv.NetworkTvDetails
import com.anshtya.core.network.model.library.FavoriteRequest
import com.anshtya.core.network.model.library.WatchlistRequest
import com.anshtya.core.network.model.search.SearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("movie/{category}")
    suspend fun getMovieLists(
        @Path("category") category: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int,
        @Query("region") region: String? = null
    ): NetworkContentResponse

    @GET("tv/{category}")
    suspend fun getTvShowLists(
        @Path("category") category: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): NetworkContentResponse

    @GET("search/multi")
    suspend fun multiSearch(
        @Query("page") page: Int = 1,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean
    ): SearchResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("append_to_response") appendToResponse: String = "recommendations,credits"
    ): NetworkMovieDetails

    @GET("tv/{series_id}")
    suspend fun getTvShowDetails(
        @Path("series_id") id: Int,
        @Query("append_to_response") appendToResponse: String = "recommendations,credits"
    ): NetworkTvDetails

    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") id: Int
    ): NetworkPersonDetails

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") accountId: Int
    ): NetworkContentResponse

    @GET("account/{account_id}/favorite/tv")
    suspend fun getFavoriteTvShows(
        @Path("account_id") accountId: Int
    ): NetworkContentResponse

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getMoviesWatchlist(
        @Path("account_id") accountId: Int
    ): NetworkContentResponse

    @GET("account/{account_id}/watchlist/tv")
    suspend fun getTvShowsWatchlist(
        @Path("account_id") accountId: Int
    ): NetworkContentResponse

    @Headers("content-type: application/json")
    @POST("account/{account_id}/favorite")
    suspend fun addOrRemoveFavorite(
        @Path("account_id") accountId: Int,
        @Body favoriteRequest: FavoriteRequest
    ): Response<Unit>

    @Headers("content-type: application/json")
    @POST("account/{account_id}/watchlist")
    suspend fun addOrRemoveFromWatchlist(
        @Path("account_id") accountId: Int,
        @Body watchlistRequest: WatchlistRequest
    ): Response<Unit>

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

    @GET("account/{account_id}")
    suspend fun getAccountDetailsWithId(
       @Path("account_id") accountId: Int
    ): NetworkAccountDetails

    @Headers("content-type: application/json")
    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(
        @Body deleteSessionRequest: DeleteSessionRequest
    )
}