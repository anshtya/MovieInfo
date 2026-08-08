package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.core.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.core.network.datasource.TmdbNetworkDataSource
import com.anshtya.movieinfo.data.local.datastore.UserPreferencesDataStore
import com.anshtya.movieinfo.data.model.SelectedDarkMode
import com.anshtya.movieinfo.data.model.user.AccountDetails
import com.anshtya.movieinfo.data.model.user.UserData
import com.anshtya.movieinfo.data.model.user.asModel
import com.anshtya.movieinfo.data.repository.UserRepository
import com.anshtya.movieinfo.data.repository.util.toResult
import com.anshtya.movieinfo.data.util.asEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val networkDataSource: TmdbNetworkDataSource,
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

    override suspend fun updateAccountDetails(accountId: Int): Result<Unit> {
        return networkDataSource.getAccountDetailsWithId(accountId).toResult { response ->
            val accountDetails = response.asEntity()
            accountDetailsDao.addAccountDetails(accountDetails)
            userPreferencesDataStore.setAdultResultPreference(accountDetails.includeAdult)
        }
    }

    override suspend fun setHideOnboarding(hideOnboarding: Boolean) {
        userPreferencesDataStore.setHideOnboarding(hideOnboarding)
    }
}