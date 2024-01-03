package com.anshtya.data.repository

import com.anshtya.core.local.datastore.UserPreferencesDataStore
import com.anshtya.core.local.shared_preferences.UserEncryptedSharedPreferences
import com.anshtya.core.network.model.DeleteSessionRequest
import com.anshtya.core.network.model.LoginRequest
import com.anshtya.core.network.model.SessionRequest
import com.anshtya.core.network.model.asModel
import com.anshtya.core.network.model.getErrorMessage
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.model.NetworkResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
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

            NetworkResponse.Success(Unit)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody().getErrorMessage()
            NetworkResponse.Error(errorMessage)
        }
    }
}