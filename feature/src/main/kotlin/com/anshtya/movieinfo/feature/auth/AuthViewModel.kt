package com.anshtya.movieinfo.feature.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.movieinfo.core.data.repository.AuthRepository
import com.anshtya.movieinfo.core.data.repository.UserRepository
import com.anshtya.movieinfo.core.model.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    var hideOnboarding: Boolean? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            hideOnboarding = userRepository.userData
                .map { it.hideOnboarding }
                .first()
        }
    }

    fun logIn() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val response = authRepository.login(
                username = _uiState.value.username,
                password = _uiState.value.password
            )
            when (response) {
                is NetworkResponse.Success -> {
                    if (hideOnboarding == false) {
                        /**
                         * User has opened app for first time. This will recompose the NavHost
                         * and user will be automatically navigated from AuthScreen.
                         */
                        setHideOnboarding()
                    } else {
                        _uiState.update {
                            it.copy(isLoggedIn = true)
                        }
                    }
                }

                is NetworkResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = response.errorMessage
                        )
                    }
                }
            }
        }
    }

    fun setHideOnboarding() {
        viewModelScope.launch {
            userRepository.setHideOnboarding(true)
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }
}

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)