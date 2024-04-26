package com.anshtya.data.repository.impl

import com.anshtya.core.local.database.dao.AccountDetailsDao
import com.anshtya.core.local.datastore.UserPreferencesDataStore
import com.anshtya.core.local.shared_preferences.UserEncryptedSharedPreferences
import com.anshtya.core.model.NetworkResponse
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.user.AccountDetails
import com.anshtya.core.model.user.UserData
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.model.asEntity
import com.anshtya.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val tmdbApi: TmdbApi,
    private val accountDetailsDao: AccountDetailsDao,
    private val userEncryptedSharedPreferences: UserEncryptedSharedPreferences
) : UserRepository {
    override val userData: Flow<UserData> = userPreferencesDataStore.userData

    override val accountDetails: Flow<AccountDetails?> = accountDetailsDao.getAccountDetails()
        .map { it?.asModel() }

    override fun isSignedIn(): Boolean = userEncryptedSharedPreferences.getSessionId() != null

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferencesDataStore.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setAdultResultPreference(includeAdultResults: Boolean) {
        userPreferencesDataStore.setAdultResultPreference(includeAdultResults)
    }

    override suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        userPreferencesDataStore.setDarkModePreference(selectedDarkMode)
    }

    override suspend fun updateAccountDetails(accountId: Int): NetworkResponse<Unit> {
        return try {
            val accountDetails = tmdbApi.getAccountDetailsWithId(accountId).asEntity()
            accountDetailsDao.addAccountDetails(accountDetails)
            userPreferencesDataStore.setAdultResultPreference(accountDetails.includeAdult)

            NetworkResponse.Success(Unit)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            NetworkResponse.Error()
        }
    }

    override suspend fun setHideOnboarding(hideOnboarding: Boolean) {
        userPreferencesDataStore.setHideOnboarding(hideOnboarding)
    }

    override suspend fun shouldHideOnboarding(): Boolean {
        return userPreferencesDataStore.userData.map { it.hideOnboarding }.first()
    }
}