package com.anshtya.movieinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.data.repository.UserDataRepository
import com.anshtya.movieinfo.MainActivityUiState.Loading
import com.anshtya.movieinfo.MainActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData.map {
        Success(
            useDynamicColor = it.useDynamicColor,
            darkMode = it.darkMode
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5000L),
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(
        val useDynamicColor: Boolean,
        val darkMode: SelectedDarkMode
    ) : MainActivityUiState
}