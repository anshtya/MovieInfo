package com.anshtya.core.local.datastore

import androidx.datastore.core.DataStore
import com.anshtya.core.local.proto.DarkMode
import com.anshtya.core.local.proto.UserPreferences
import com.anshtya.core.local.proto.copy
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.user.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataStore @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data
        .map {
            UserData(
                useDynamicColor = it.useDynamicColor,
                includeAdultResults = it.includeAdultResults,
                darkMode = when (it.darkMode) {
                    null,
                    DarkMode.UNRECOGNIZED,
                    DarkMode.DARK_MODE_SYSTEM
                    -> SelectedDarkMode.SYSTEM

                    DarkMode.DARK_MODE_DARK -> SelectedDarkMode.DARK
                    DarkMode.DARK_MODE_LIGHT -> SelectedDarkMode.LIGHT
                }
            )
        }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.copy { this.useDynamicColor = useDynamicColor }
        }
    }

    suspend fun setAdultResultPreference(includeAdultResults: Boolean) {
        userPreferences.updateData {
            it.copy { this.includeAdultResults = includeAdultResults }
        }
    }

    suspend fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        userPreferences.updateData {
            it.copy {
                this.darkMode = when (selectedDarkMode) {
                    SelectedDarkMode.SYSTEM -> DarkMode.DARK_MODE_SYSTEM
                    SelectedDarkMode.DARK -> DarkMode.DARK_MODE_DARK
                    SelectedDarkMode.LIGHT -> DarkMode.DARK_MODE_LIGHT
                }
            }
        }
    }
}