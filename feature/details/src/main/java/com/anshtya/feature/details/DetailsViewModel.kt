package com.anshtya.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.NetworkResponse
import com.anshtya.core.model.details.MovieDetails
import com.anshtya.core.model.details.people.PersonDetails
import com.anshtya.core.model.details.tv.TvDetails
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.data.repository.DetailsRepository
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.data.repository.UserRepository
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
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val detailsRepository: DetailsRepository,
    private val libraryRepository: LibraryRepository,
    private val userRepository: UserRepository
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

    fun addOrRemoveFavorite(libraryItem: LibraryItem) {
        viewModelScope.launch {
            try {
                val isSignedIn = userRepository.isSignedIn()
                if (isSignedIn) {
                    _uiState.update { it.copy(markedFavorite = !(it.markedFavorite)) }
                    libraryRepository.addOrRemoveFavorite(libraryItem)
                } else {
                    _uiState.update { it.copy(showSignInSheet = true) }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        markedFavorite = !(it.markedFavorite),
                        errorMessage = "An error occurred"
                    )
                }
            }
        }
    }

    fun addOrRemoveFromWatchlist(libraryItem: LibraryItem) {
        viewModelScope.launch {
            try {
                val isSignedIn = userRepository.isSignedIn()
                if (isSignedIn) {
                    _uiState.update { it.copy(savedInWatchlist = !(it.savedInWatchlist)) }
                    libraryRepository.addOrRemoveFromWatchlist(libraryItem)
                } else {
                    _uiState.update { it.copy(showSignInSheet = true) }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        savedInWatchlist = !(it.savedInWatchlist),
                        errorMessage = "An error occurred"
                    )
                }
            }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onHideBottomSheet() {
        _uiState.update { it.copy(showSignInSheet = false) }
    }

    private fun getIdAndMediaType(detailsString: String): Pair<Int, MediaType?> {
        val details = detailsString.split(",")
        val id = details.first().toInt()
        val mediaType = enumValueOf<MediaType>(details.last())
        return Pair(id, mediaType)
    }

    private suspend fun handleMovieDetailsResponse(
        response: NetworkResponse<MovieDetails>
    ): ContentDetailUiState {
        return when (response) {
            is NetworkResponse.Success -> {
                val data = response.data
                _uiState.update {
                    it.copy(
                        markedFavorite = libraryRepository.favoriteItemExists(data.id),
                        savedInWatchlist = libraryRepository.itemInWatchlistExists(data.id)
                    )
                }
                ContentDetailUiState.Movie(data = data)
            }

            is NetworkResponse.Error -> {
                _uiState.update { it.copy(errorMessage = response.errorMessage) }
                ContentDetailUiState.Empty
            }
        }
    }

    private suspend fun handleTvDetailsResponse(
        response: NetworkResponse<TvDetails>
    ): ContentDetailUiState {
        return when (response) {
            is NetworkResponse.Success -> {
                val data = response.data
                _uiState.update {
                    it.copy(
                        markedFavorite = libraryRepository.favoriteItemExists(data.id),
                        savedInWatchlist = libraryRepository.itemInWatchlistExists(data.id)
                    )
                }
                ContentDetailUiState.TV(data = data)
            }

            is NetworkResponse.Error -> {
                _uiState.update { it.copy(errorMessage = response.errorMessage) }
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
                _uiState.update { it.copy(errorMessage = response.errorMessage) }
                ContentDetailUiState.Empty
            }
        }
    }
}

data class DetailsUiState(
    val isLoading: Boolean = false,
    val markedFavorite: Boolean = false,
    val savedInWatchlist: Boolean = false,
    val showSignInSheet: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ContentDetailUiState {
    data object Empty : ContentDetailUiState
    data class Movie(val data: MovieDetails) : ContentDetailUiState
    data class TV(val data: TvDetails) : ContentDetailUiState
    data class Person(val data: PersonDetails) : ContentDetailUiState
}