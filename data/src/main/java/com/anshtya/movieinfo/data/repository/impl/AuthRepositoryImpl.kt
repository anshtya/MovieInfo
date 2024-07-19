package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.core.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.core.local.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.core.local.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.core.local.datastore.UserPreferencesDataStore
import com.anshtya.movieinfo.core.local.session.SessionManager
import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.network.model.auth.DeleteSessionRequest
import com.anshtya.movieinfo.core.network.model.auth.LoginRequest
import com.anshtya.movieinfo.core.network.model.auth.SessionRequest
import com.anshtya.movieinfo.core.network.model.auth.getErrorMessage
import com.anshtya.movieinfo.core.network.retrofit.TmdbApi
import com.anshtya.movieinfo.data.model.asEntity
import com.anshtya.movieinfo.data.repository.AuthRepository
import com.anshtya.movieinfo.data.util.SyncScheduler
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val favoriteContentDao: FavoriteContentDao,
    private val watchlistContentDao: WatchlistContentDao,
    private val accountDetailsDao: AccountDetailsDao,
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val sessionManager: SessionManager,
    private val syncScheduler: SyncScheduler
) : AuthRepository {
    override val isLoggedIn = sessionManager.isLoggedIn

    override suspend fun login(
        username: String,
        password: String
    ): NetworkResponse<Unit> {
        return try {
            val response = tmdbApi.createRequestToken()
            val loginRequest = LoginRequest(
                username = username,
                password = password,
                requestToken = response.requestToken
            )
            val loginResponse = tmdbApi.validateWithLogin(loginRequest)

            val sessionRequest = SessionRequest(loginResponse.requestToken)
            val sessionResponse = tmdbApi.createSession(sessionRequest)

            val accountDetails =
                tmdbApi.getAccountDetails(sessionResponse.sessionId).asEntity()

            sessionManager.storeSessionId(sessionResponse.sessionId)
            accountDetailsDao.addAccountDetails(accountDetails)
            userPreferencesDataStore.setAdultResultPreference(accountDetails.includeAdult)

            syncScheduler.scheduleLibrarySyncWork()

            NetworkResponse.Success(Unit)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            val errorMessage = getErrorMessage(e)
            NetworkResponse.Error(errorMessage)
        }
    }

    override suspend fun logout(accountId: Int): NetworkResponse<Unit> {
        return try {
            val sessionId = sessionManager.getSessionId()!!
            val deleteSessionRequest = DeleteSessionRequest(sessionId)

            tmdbApi.deleteSession(deleteSessionRequest)
            sessionManager.deleteSessionId()
            accountDetailsDao.deleteAccountDetails(accountId)

            favoriteContentDao.deleteAllFavoriteItems()
            watchlistContentDao.deleteAllWatchlistItems()

            NetworkResponse.Success(Unit)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            val errorMessage = getErrorMessage(e)
            NetworkResponse.Error(errorMessage)
        }
    }
}