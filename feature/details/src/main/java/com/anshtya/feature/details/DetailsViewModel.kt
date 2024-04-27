package com.anshtya.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.NetworkResponse
import com.anshtya.core.model.details.MovieDetails
import com.anshtya.core.model.details.PersonDetails
import com.anshtya.core.model.details.tv.TvDetails
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.core.model.library.LibraryTask
import com.anshtya.data.repository.DetailsRepository
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.data.util.SyncManager
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
    private val syncManager: SyncManager
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

    fun addOrRemoveFavorite(libraryItem: LibraryItem) {
        viewModelScope.launch {
            try {
                val itemExists = libraryRepository.addOrRemoveFavorite(libraryItem)
                val libraryTask = LibraryTask.favoriteItemTask(
                    mediaId = libraryItem.id,
                    mediaType = enumValueOf(libraryItem.mediaType),
                    itemExists = !itemExists
                )
                syncManager.scheduleLibraryTaskWork(libraryTask)
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(errorMessage = "An error occurred")
                }
            }
        }
    }

    fun addOrRemoveFromWatchlist(libraryItem: LibraryItem) {
        viewModelScope.launch {
            try {
                val itemExists = libraryRepository.addOrRemoveFromWatchlist(libraryItem)
                val libraryTask = LibraryTask.watchlistItemTask(
                    mediaId = libraryItem.id,
                    mediaType = enumValueOf(libraryItem.mediaType),
                    itemExists = !itemExists
                )
                syncManager.scheduleLibraryTaskWork(libraryTask)
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(errorMessage = "An error occurred")
                }
            }
        }
    }

    private fun getIdAndMediaType(detailsString: String): Pair<Int, MediaType?> {
        val details = detailsString.split(",")
        val id = details.first().toInt()
        val mediaType = enumValueOf<MediaType>(details.last())
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
                _uiState.update { it.copy(errorMessage = response.errorMessage) }
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
    val errorMessage: String? = null
)

sealed interface ContentDetailUiState {
    data object Empty : ContentDetailUiState
    data class Movie(val data: MovieDetails) : ContentDetailUiState
    data class TV(val data: TvDetails) : ContentDetailUiState
    data class Person(val data: PersonDetails) : ContentDetailUiState
}