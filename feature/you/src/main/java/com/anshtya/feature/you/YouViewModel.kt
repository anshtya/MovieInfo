package com.anshtya.feature.you

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.user.AccountDetails
import com.anshtya.core.ui.ErrorText
import com.anshtya.data.model.NetworkResponse
import com.anshtya.data.repository.AuthRepository
import com.anshtya.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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

    private val viewModelState = MutableStateFlow(YouViewModelState())

    val uiState = viewModelState
        .map(YouViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = viewModelState.value.toUiState()
        )

    val userSettings = userDataRepository.userData
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

    init {
        getUserData()
    }

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
            viewModelState.update { it.copy(isLoading = true) }
            when (val response = authRepository.logout()) {
                is NetworkResponse.Success -> {}
                is NetworkResponse.Error -> {
                    val errorMessage = response.errorMessage?.let {
                        ErrorText.SimpleText(it)
                    } ?: ErrorText.StringResource(id = R.string.error_message)

                    viewModelState.update { it.copy(errorMessage = errorMessage) }
                }
            }
            viewModelState.update { it.copy(isLoading = false) }
            logOutJob = null
        }
    }

    fun onErrorShown() {
        viewModelState.update { it.copy(errorMessage = null) }
    }

    private fun getUserData() {
        userDataRepository.userData
            .distinctUntilChanged()
            .onEach { userData ->
                viewModelState.update {
                    it.copy(
                        isLoggedIn = userData.isLoggedIn,
                        accountDetails = userData.accountDetails
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}

sealed interface YouUiState {

    val errorMessage: ErrorText?

    data class LoggedIn(
        val accountDetails: AccountDetails,
        val isLoading: Boolean,
        override val errorMessage: ErrorText?
    ) : YouUiState

    data class LoggedOff(
        override val errorMessage: ErrorText?
    ) : YouUiState
}

data class UserSettings(
    val useDynamicColor: Boolean,
    val includeAdultResults: Boolean,
    val darkMode: SelectedDarkMode
)

private data class YouViewModelState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean? = null,
    val accountDetails: AccountDetails? = null,
    val errorMessage: ErrorText? = null
) {
    fun toUiState(): YouUiState =
        if (isLoggedIn == true) {
            YouUiState.LoggedIn(
                accountDetails = accountDetails!!,
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        } else {
            YouUiState.LoggedOff(errorMessage = errorMessage)
        }
}