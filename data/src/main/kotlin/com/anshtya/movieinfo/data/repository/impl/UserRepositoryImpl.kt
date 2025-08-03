package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.data.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.data.local.database.entity.asEntity
import com.anshtya.movieinfo.data.local.datastore.UserPreferencesDataStore
import com.anshtya.movieinfo.data.model.NetworkResponse
import com.anshtya.movieinfo.data.model.SelectedDarkMode
import com.anshtya.movieinfo.data.model.user.AccountDetails
import com.anshtya.movieinfo.data.model.user.UserData
import com.anshtya.movieinfo.data.network.retrofit.TmdbApi
import com.anshtya.movieinfo.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val tmdbApi: TmdbApi,
    private val accountDetailsDao: AccountDetailsDao,
) : UserRepository {
    override val userData: Flow<UserData> = userPreferencesDataStore.userData

    override suspend fun getAccountDetails(): AccountDetails? = accountDetailsDao
        .getAccountDetails()?.asModel()

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
}