package com.anshtya.movieinfo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.data.repository.UserRepository
import com.anshtya.movieinfo.MainActivityUiState.Loading
import com.anshtya.movieinfo.MainActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userRepository.userData.map {
        Success(
            useDynamicColor = it.useDynamicColor,
            darkMode = it.darkMode
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5000L),
    )

    var hideOnboarding: Boolean? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            hideOnboarding = userRepository.userData
                .map { it.hideOnboarding }
                .first()
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(
        val useDynamicColor: Boolean,
        val darkMode: SelectedDarkMode
    ) : MainActivityUiState
}