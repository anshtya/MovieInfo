package com.anshtya.data.repository.impl

import com.anshtya.core.local.datastore.UserPreferencesDataStore
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.user.AccountDetails
import com.anshtya.core.model.user.UserData
import com.anshtya.data.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : UserDataRepository {
    override val userData: Flow<UserData> = userPreferencesDataStore.userData

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferencesDataStore.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setAdultResultPreference(includeAdultResults: Boolean) {
        userPreferencesDataStore.setAdultResultPreference(includeAdultResults)
    }

    override suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        userPreferencesDataStore.setDarkModePreference(selectedDarkMode)
    }

    override suspend fun saveAccountDetails(accountDetails: AccountDetails) {
        userPreferencesDataStore.saveAccountDetails(accountDetails)
    }

    override suspend fun removeAccountDetails() {
        userPreferencesDataStore.removeAccountDetails()
    }
}