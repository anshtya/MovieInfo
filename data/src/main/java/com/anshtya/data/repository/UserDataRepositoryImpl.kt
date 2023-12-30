package com.anshtya.data.repository

import com.anshtya.core.local.datastore.UserPreferencesDataStore
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : UserDataRepository  {
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
}