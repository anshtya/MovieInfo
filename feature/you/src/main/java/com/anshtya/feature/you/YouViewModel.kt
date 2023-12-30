package com.anshtya.feature.you

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.data.repository.UserDataRepository
import com.anshtya.feature.you.SettingsUiState.Loading
import com.anshtya.feature.you.SettingsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YouViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    val settingsUiState = userDataRepository.userData
        .map {
            Success(
                userSettings = UserSettings(
                    useDynamicColor = it.useDynamicColor,
                    includeAdultResults = it.includeAdultResults,
                    darkMode = it.darkMode
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Loading
        )

    fun setDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
        }
    }

    fun setAdultResultPreference(includeAdultResults: Boolean) {
        viewModelScope.launch {
            userDataRepository.setAdultResultPreference(includeAdultResults)
        }
    }

    fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        viewModelScope.launch {
            userDataRepository.setDarkModePreference(selectedDarkMode)
        }
    }

}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val userSettings: UserSettings) : SettingsUiState
}

data class UserSettings(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val darkMode: SelectedDarkMode
)