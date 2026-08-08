package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.core.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.core.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.core.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.core.network.datasource.TmdbNetworkDataSource
import com.anshtya.movieinfo.core.network.model.auth.DeleteSessionRequest
import com.anshtya.movieinfo.core.network.model.auth.LoginRequest
import com.anshtya.movieinfo.core.network.model.auth.SessionRequest
import com.anshtya.movieinfo.data.local.datastore.UserPreferencesDataStore
import com.anshtya.movieinfo.data.local.session.SessionManager
import com.anshtya.movieinfo.data.repository.AuthRepository
import com.anshtya.movieinfo.data.repository.util.SyncScheduler
import com.anshtya.movieinfo.data.repository.util.toResult
import com.anshtya.movieinfo.data.util.asEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val networkDataSource: TmdbNetworkDataSource,
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
    ): Result<Unit> {
        val tokenResponse = networkDataSource.createRequestToken()
            .toResult { it }
            .getOrElse { return Result.failure(it) }
        val loginRequest = LoginRequest(
            username = username,
            password = password,
            requestToken = tokenResponse.requestToken
        )
        val loginResponse = networkDataSource.validateWithLogin(loginRequest)
            .toResult { it }
            .getOrElse { return Result.failure(it) }

        val sessionRequest = SessionRequest(loginResponse.requestToken)
        val sessionResponse = networkDataSource.createSession(sessionRequest)
            .toResult { it }
            .getOrElse { return Result.failure(it) }

        val accountDetails = networkDataSource.getAccountDetails(sessionResponse.sessionId)
            .toResult { it }
            .getOrElse { return Result.failure(it) }
            .asEntity()

        sessionManager.storeSessionId(sessionResponse.sessionId)
        accountDetailsDao.addAccountDetails(accountDetails)
        userPreferencesDataStore.setAdultResultPreference(accountDetails.includeAdult)

        syncScheduler.scheduleLibrarySyncWork()

        return Result.success(Unit)
    }

    override suspend fun logout(accountId: Int): Result<Unit> {
        val sessionId = sessionManager.getSessionId()!!
        val deleteSessionRequest = DeleteSessionRequest(sessionId)

        return networkDataSource.deleteSession(deleteSessionRequest).toResult {
            sessionManager.deleteSessionId()
            accountDetailsDao.deleteAccountDetails(accountId)

            favoriteContentDao.deleteAllFavoriteItems()
            watchlistContentDao.deleteAllWatchlistItems()
        }
    }
}
