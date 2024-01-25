package com.anshtya.data.repository.impl

import com.anshtya.core.local.database.dao.FavoriteContentDao
import com.anshtya.core.local.database.dao.WatchlistContentDao
import com.anshtya.core.local.datastore.UserPreferencesDataStore
import com.anshtya.core.local.shared_preferences.UserEncryptedSharedPreferences
import com.anshtya.core.network.model.auth.DeleteSessionRequest
import com.anshtya.core.network.model.auth.LoginRequest
import com.anshtya.core.network.model.auth.SessionRequest
import com.anshtya.core.network.model.auth.asModel
import com.anshtya.core.network.model.getErrorMessage
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.model.NetworkResponse
import com.anshtya.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val favoriteContentDao: FavoriteContentDao,
    private val watchlistContentDao: WatchlistContentDao,
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val encryptedSharedPreferences: UserEncryptedSharedPreferences
) : AuthRepository {
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
                tmdbApi.getAccountDetails(sessionResponse.sessionId).asModel()

            encryptedSharedPreferences.storeSessionId(sessionResponse.sessionId)
            userPreferencesDataStore.saveAccountDetails(accountDetails)

            NetworkResponse.Success(Unit)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody().getErrorMessage()
            NetworkResponse.Error(errorMessage)
        }
    }

    override suspend fun logout(): NetworkResponse<Unit> {
        return try {
            val sessionId = encryptedSharedPreferences.getSessionId()!!
            val deleteSessionRequest = DeleteSessionRequest(sessionId)

            tmdbApi.deleteSession(deleteSessionRequest)
            encryptedSharedPreferences.deleteSessionId()
            userPreferencesDataStore.removeAccountDetails()

            favoriteContentDao.deleteAllItems()
            watchlistContentDao.deleteAllItems()

            NetworkResponse.Success(Unit)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody().getErrorMessage()
            NetworkResponse.Error(errorMessage)
        }
    }

    override suspend fun updateAccountDetails(): NetworkResponse<Unit> {
        return try {
            val userData = userPreferencesDataStore.userData.first()
            val localAccountDetails = userData.accountDetails
            val networkAccountDetails = tmdbApi.getAccountDetailsWithId(localAccountDetails.id).asModel()

            if (networkAccountDetails != localAccountDetails) {
                userPreferencesDataStore.saveAccountDetails(networkAccountDetails)
            }

            NetworkResponse.Success(Unit)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            NetworkResponse.Error()
        }
    }
}