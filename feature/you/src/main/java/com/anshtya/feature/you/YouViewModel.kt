package com.anshtya.feature.you

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.user.AccountDetails
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.ui.ErrorText
import com.anshtya.data.model.NetworkResponse
import com.anshtya.data.repository.AuthRepository
import com.anshtya.data.repository.UserDataRepository
import com.anshtya.feature.you.YouUiState.Loading
import com.anshtya.feature.you.YouUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YouViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private var logOutJob: Job? = null

    private val _errorMessage = MutableStateFlow<ErrorText?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val uiState = userDataRepository.userData
        .map {
            Success(
                isLoggedIn = it.isLoggedIn,
                accountDetails = it.accountDetails,
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

    fun logOut() {
        if (logOutJob != null) return

        logOutJob = viewModelScope.launch {
            when (val response = authRepository.logout()) {
                is NetworkResponse.Success -> {}
                is NetworkResponse.Error -> {
                    val errorMessage = response.errorMessage?.let {
                        ErrorText.SimpleText(it)
                    } ?: ErrorText.StringResource(id = R.string.error_message)

                    _errorMessage.update { errorMessage }
                }
            }
            logOutJob = null
        }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }
}

sealed interface YouUiState {
    data object Loading : YouUiState
    data class Success(
        val isLoggedIn: Boolean,
        val accountDetails: AccountDetails,
        val userSettings: UserSettings
    ) : YouUiState
}

data class UserSettings(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val darkMode: SelectedDarkMode
)