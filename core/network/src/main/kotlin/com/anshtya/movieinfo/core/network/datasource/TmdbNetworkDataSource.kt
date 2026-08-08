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

interface TmdbNetworkDataSource {
    suspend fun getMovieLists(
        category: String,
        language: String = "en-US",
        page: Int,
        region: String? = null
    ): NetworkResult<NetworkContentResponse>

    suspend fun getTvShowLists(
        category: String,
        language: String = "en-US",
        page: Int
    ): NetworkResult<NetworkContentResponse>

    suspend fun multiSearch(
        page: Int = 1,
        query: String,
        includeAdult: Boolean
    ): NetworkResult<SearchResponse>

    suspend fun getMovieDetails(id: Int): NetworkResult<NetworkMovieDetails>

    suspend fun getTvShowDetails(id: Int): NetworkResult<NetworkTvDetails>

    suspend fun getPersonDetails(id: Int): NetworkResult<NetworkPersonDetails>

    suspend fun getLibraryItems(
        accountId: Int,
        itemType: String,
        mediaType: String,
        page: Int
    ): NetworkResult<NetworkContentResponse>

    suspend fun addOrRemoveFavorite(
        accountId: Int,
        favoriteRequest: FavoriteRequest
    ): NetworkResult<Unit>

    suspend fun addOrRemoveFromWatchlist(
        accountId: Int,
        watchlistRequest: WatchlistRequest
    ): NetworkResult<Unit>

    suspend fun createRequestToken(): NetworkResult<RequestTokenResponse>

    suspend fun validateWithLogin(loginRequest: LoginRequest): NetworkResult<LoginResponse>

    suspend fun createSession(sessionRequest: SessionRequest): NetworkResult<SessionResponse>

    suspend fun getAccountDetails(sessionId: String): NetworkResult<NetworkAccountDetails>

    suspend fun getAccountDetailsWithId(accountId: Int): NetworkResult<NetworkAccountDetails>

    suspend fun deleteSession(deleteSessionRequest: DeleteSessionRequest): NetworkResult<Unit>
}
