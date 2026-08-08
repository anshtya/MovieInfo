package com.anshtya.movieinfo.core.network.datasource

import com.anshtya.movieinfo.core.network.model.NetworkResult
import com.anshtya.movieinfo.core.network.model.auth.DeleteSessionRequest
import com.anshtya.movieinfo.core.network.model.auth.LoginRequest
import com.anshtya.movieinfo.core.network.model.auth.LoginResponse
import com.anshtya.movieinfo.core.network.model.auth.NetworkAccountDetails
import com.anshtya.movieinfo.core.network.model.auth.RequestTokenResponse
import com.anshtya.movieinfo.core.network.model.auth.SessionRequest
import com.anshtya.movieinfo.core.network.model.auth.SessionResponse
import com.anshtya.movieinfo.core.network.model.content.NetworkContentResponse
import com.anshtya.movieinfo.core.network.model.details.NetworkMovieDetails
import com.anshtya.movieinfo.core.network.model.details.people.NetworkPersonDetails
import com.anshtya.movieinfo.core.network.model.details.tv.NetworkTvDetails
import com.anshtya.movieinfo.core.network.model.library.FavoriteRequest
import com.anshtya.movieinfo.core.network.model.library.WatchlistRequest
import com.anshtya.movieinfo.core.network.model.search.SearchResponse
import com.anshtya.movieinfo.core.network.retrofit.TmdbApi
import com.anshtya.movieinfo.core.network.util.safeApiCall
import javax.inject.Inject

internal class TmdbNetworkDataSourceImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : TmdbNetworkDataSource {
    override suspend fun getMovieLists(
        category: String,
        language: String,
        page: Int,
        region: String?
    ): NetworkResult<NetworkContentResponse> = safeApiCall {
        tmdbApi.getMovieLists(category = category, language = language, page = page, region = region)
    }

    override suspend fun getTvShowLists(
        category: String,
        language: String,
        page: Int
    ): NetworkResult<NetworkContentResponse> = safeApiCall {
        tmdbApi.getTvShowLists(category = category, language = language, page = page)
    }

    override suspend fun multiSearch(
        page: Int,
        query: String,
        includeAdult: Boolean
    ): NetworkResult<SearchResponse> = safeApiCall {
        tmdbApi.multiSearch(page = page, query = query, includeAdult = includeAdult)
    }

    override suspend fun getMovieDetails(id: Int): NetworkResult<NetworkMovieDetails> = safeApiCall {
        tmdbApi.getMovieDetails(id)
    }

    override suspend fun getTvShowDetails(id: Int): NetworkResult<NetworkTvDetails> = safeApiCall {
        tmdbApi.getTvShowDetails(id)
    }

    override suspend fun getPersonDetails(id: Int): NetworkResult<NetworkPersonDetails> = safeApiCall {
        tmdbApi.getPersonDetails(id)
    }

    override suspend fun getLibraryItems(
        accountId: Int,
        itemType: String,
        mediaType: String,
        page: Int
    ): NetworkResult<NetworkContentResponse> = safeApiCall {
        tmdbApi.getLibraryItems(
            accountId = accountId,
            itemType = itemType,
            mediaType = mediaType,
            page = page
        )
    }

    override suspend fun addOrRemoveFavorite(
        accountId: Int,
        favoriteRequest: FavoriteRequest
    ): NetworkResult<Unit> = safeApiCall {
        tmdbApi.addOrRemoveFavorite(accountId, favoriteRequest)
    }

    override suspend fun addOrRemoveFromWatchlist(
        accountId: Int,
        watchlistRequest: WatchlistRequest
    ): NetworkResult<Unit> = safeApiCall {
        tmdbApi.addOrRemoveFromWatchlist(accountId, watchlistRequest)
    }

    override suspend fun createRequestToken(): NetworkResult<RequestTokenResponse> = safeApiCall {
        tmdbApi.createRequestToken()
    }

    override suspend fun validateWithLogin(
        loginRequest: LoginRequest
    ): NetworkResult<LoginResponse> = safeApiCall {
        tmdbApi.validateWithLogin(loginRequest)
    }

    override suspend fun createSession(
        sessionRequest: SessionRequest
    ): NetworkResult<SessionResponse> = safeApiCall {
        tmdbApi.createSession(sessionRequest)
    }

    override suspend fun getAccountDetails(
        sessionId: String
    ): NetworkResult<NetworkAccountDetails> = safeApiCall {
        tmdbApi.getAccountDetails(sessionId)
    }

    override suspend fun getAccountDetailsWithId(
        accountId: Int
    ): NetworkResult<NetworkAccountDetails> = safeApiCall {
        tmdbApi.getAccountDetailsWithId(accountId)
    }

    override suspend fun deleteSession(
        deleteSessionRequest: DeleteSessionRequest
    ): NetworkResult<Unit> = safeApiCall {
        tmdbApi.deleteSession(deleteSessionRequest)
    }
}
