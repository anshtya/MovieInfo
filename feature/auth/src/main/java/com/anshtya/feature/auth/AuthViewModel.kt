package com.anshtya.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.ui.ErrorText
import com.anshtya.data.model.NetworkResponse
import com.anshtya.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private var logInJob: Job? = null

    fun logIn() {
        if (logInJob != null) return

        logInJob = viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }
            val response = authRepository.login(
                username = _uiState.value.username,
                password = _uiState.value.password
            )
            when(response) {
                is NetworkResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = true,
                            isLoading = false
                        )
                    }
                }
                is NetworkResponse.Error -> {
                    val errorMessage = response.errorMessage?.let {
                        ErrorText.SimpleText(it)
                    } ?: ErrorText.StringResource(id = R.string.error_message)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                }
            }
            logInJob = null
        }
    }

    fun onErrorShown() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun onUsernameChange(username: String) {
        _uiState.update {
            it.copy(username = username)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }
}

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val errorMessage: ErrorText? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean? = null
)