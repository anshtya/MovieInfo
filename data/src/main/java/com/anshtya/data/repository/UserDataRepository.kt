package com.anshtya.data.repository

import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
    suspend fun setAdultResultPreference(includeAdultResults: Boolean)
    suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode)
}