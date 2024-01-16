package com.anshtya.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.MovieDetails
import com.anshtya.core.model.PersonDetails
import com.anshtya.core.model.TvDetails
import com.anshtya.core.ui.ErrorText
import com.anshtya.data.model.NetworkResponse
import com.anshtya.data.repository.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val detailsRepository: DetailsRepository
) : ViewModel() {
    private val idDetailsString = savedStateHandle.getStateFlow(
        key = idNavigationArgument,
        initialValue = ""
    )

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    val contentDetailsUiState: StateFlow<ContentDetailUiState> = idDetailsString
        .onStart { _uiState.update { it.copy(isLoading = true) } }
        .mapLatest { detailsString ->
            detailsString.takeIf { it.isNotEmpty() }?.let {
                val idDetails = getIdAndMediaType(detailsString)

                val id = idDetails.first
                when (idDetails.second) {
                    MediaType.MOVIE -> {
                        val response = detailsRepository.getMovieDetails(id)
                        handleMovieDetailsResponse(response)
                    }

                    MediaType.TV -> {
                        val response = detailsRepository.getTvShowDetails(id)
                        handleTvDetailsResponse(response)
                    }

                    MediaType.PERSON -> {
                        val response = detailsRepository.getPersonDetails(id)
                        handlePeopleDetailsResponse(response)
                    }
                    else -> ContentDetailUiState.Empty
                }
            } ?: ContentDetailUiState.Empty
        }
        .onEach { _uiState.update { it.copy(isLoading = false) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ContentDetailUiState.Empty
        )

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun getIdAndMediaType(detailsString: String): Pair<Int, MediaType?> {
        val details = detailsString.split(",")
        val id = details.first().toInt()
        val mediaType = when(details.last()) {
            "movie" -> MediaType.MOVIE
            "tv" -> MediaType.TV
            "person" -> MediaType.PERSON
            else -> null
        }
        return Pair(id, mediaType)
    }

    private fun handleMovieDetailsResponse(
        response: NetworkResponse<MovieDetails>
    ): ContentDetailUiState {
        return when (response) {
            is NetworkResponse.Success -> {
                ContentDetailUiState.Movie(data = response.data)
            }

            is NetworkResponse.Error -> {
                val error = ErrorText.StringResource(id = R.string.error_message)
                _uiState.update { it.copy(errorMessage = error) }
                ContentDetailUiState.Empty
            }
        }
    }

    private fun handleTvDetailsResponse(
        response: NetworkResponse<TvDetails>
    ): ContentDetailUiState {
        return when (response) {
            is NetworkResponse.Success -> {
                ContentDetailUiState.TV(data = response.data)
            }

            is NetworkResponse.Error -> {
                val error = ErrorText.StringResource(id = R.string.error_message)
                _uiState.update { it.copy(errorMessage = error) }
                ContentDetailUiState.Empty
            }
        }
    }

    private fun handlePeopleDetailsResponse(
        response: NetworkResponse<PersonDetails>
    ): ContentDetailUiState {
        return when (response) {
            is NetworkResponse.Success -> {
                ContentDetailUiState.Person(data = response.data)
            }

            is NetworkResponse.Error -> {
                val error = ErrorText.StringResource(id = R.string.error_message)
                _uiState.update { it.copy(errorMessage = error) }
                ContentDetailUiState.Empty
            }
        }
    }
}

data class DetailsUiState(
    val isLoading: Boolean = false,
    val errorMessage: ErrorText? = null
)

sealed interface ContentDetailUiState {
    data object Empty : ContentDetailUiState
    data class Movie(val data: MovieDetails) : ContentDetailUiState
    data class TV(val data: TvDetails) : ContentDetailUiState
    data class Person(val data: PersonDetails) : ContentDetailUiState
}