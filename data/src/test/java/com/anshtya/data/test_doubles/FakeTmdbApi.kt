package com.anshtya.data.test_doubles

import com.anshtya.core.network.model.auth.DeleteSessionRequest
import com.anshtya.core.network.model.auth.LoginRequest
import com.anshtya.core.network.model.auth.LoginResponse
import com.anshtya.core.network.model.auth.NetworkAccountDetails
import com.anshtya.core.network.model.auth.RequestTokenResponse
import com.anshtya.core.network.model.auth.SessionRequest
import com.anshtya.core.network.model.auth.SessionResponse
import com.anshtya.core.network.model.content.NetworkContentResponse
import com.anshtya.core.network.model.details.NetworkMovieDetails
import com.anshtya.core.network.model.details.NetworkPersonDetails
import com.anshtya.core.network.model.details.tv.NetworkTvDetails
import com.anshtya.core.network.model.library.FavoriteRequest
import com.anshtya.core.network.model.library.WatchlistRequest
import com.anshtya.core.network.model.search.SearchResponse
import com.anshtya.core.network.retrofit.TmdbApi

internal class FakeTmdbApi(
    private val movieListResponse: NetworkContentResponse
): TmdbApi {
    override suspend fun getMovieLists(
        category: String,
        language: String,
        page: Int
    ): NetworkContentResponse {
        return movieListResponse
    }

    override suspend fun getTvShowLists(
        category: String,
        language: String,
        page: Int
    ): NetworkContentResponse {
        TODO("Not yet implemented")
    }

    override suspend fun multiSearch(
        page: Int,
        query: String,
        includeAdult: Boolean
    ): SearchResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieDetails(id: Int): NetworkMovieDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowDetails(id: Int): NetworkTvDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getPersonDetails(id: Int): NetworkPersonDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteMovies(accountId: Int): NetworkContentResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteTvShows(accountId: Int): NetworkContentResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getMoviesWatchlist(accountId: Int): NetworkContentResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowsWatchlist(accountId: Int): NetworkContentResponse {
        TODO("Not yet implemented")
    }

    override suspend fun addOrRemoveFavorite(accountId: Int, favoriteRequest: FavoriteRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun addOrRemoveFromWatchlist(
        accountId: Int,
        watchlistRequest: WatchlistRequest
    ) {
        return
    }

    override suspend fun createRequestToken(): RequestTokenResponse {
        TODO("Not yet implemented")
    }

    override suspend fun validateWithLogin(loginRequest: LoginRequest): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createSession(sessionRequest: SessionRequest): SessionResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountDetails(sessionId: String): NetworkAccountDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountDetailsWithId(accountId: Int): NetworkAccountDetails {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSession(deleteSessionRequest: DeleteSessionRequest) {
        TODO("Not yet implemented")
    }
}