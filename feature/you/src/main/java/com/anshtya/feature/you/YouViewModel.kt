package com.anshtya.feature.you

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.NetworkResponse
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.data.repository.AuthRepository
import com.anshtya.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YouViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(YouUiState())
    val uiState = _uiState.asStateFlow()

    var isSignedIn by mutableStateOf(userRepository.isSignedIn())

    val accountDetails = userRepository.accountDetails
        .onEach { isSignedIn = userRepository.isSignedIn() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val userSettings = userRepository.userData
        .map {
            UserSettings(
                useDynamicColor = it.useDynamicColor,
                includeAdultResults = it.includeAdultResults,
                darkMode = it.darkMode
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    fun setDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userRepository.setDynamicColorPreference(useDynamicColor)
        }
    }

    fun setAdultResultPreference(includeAdultResults: Boolean) {
        viewModelScope.launch {
            userRepository.setAdultResultPreference(includeAdultResults)
        }
    }

    fun setDarkModePreference(selectedDarkMode: SelectedDarkMode) {
        viewModelScope.launch {
            userRepository.setDarkModePreference(selectedDarkMode)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true) }

            val response = authRepository.logout(accountId = accountDetails.value!!.id)
            when (response) {
                is NetworkResponse.Success -> {}

                is NetworkResponse.Error -> {
                    val errorMessage = response.errorMessage
                    _uiState.update { it.copy(errorMessage = errorMessage) }
                }
            }

            _uiState.update { it.copy(isLoggingOut = false) }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            val response =
                userRepository.updateAccountDetails(accountId = accountDetails.value!!.id)
            when (response) {
                is NetworkResponse.Success -> {}

                is NetworkResponse.Error -> {
                    val errorMessage = response.errorMessage
                    _uiState.update { it.copy(errorMessage = errorMessage) }
                }
            }

            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

data class YouUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoggingOut: Boolean = false,
    val errorMessage: String? = null
)

data class UserSettings(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val darkMode: SelectedDarkMode,
)