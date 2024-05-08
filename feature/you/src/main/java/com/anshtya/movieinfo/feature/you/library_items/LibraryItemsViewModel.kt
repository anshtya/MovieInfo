package com.anshtya.movieinfo.feature.you.library_items

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.movieinfo.core.model.library.LibraryItem
import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.data.repository.LibraryRepository
import com.anshtya.movieinfo.feature.you.libraryItemTypeNavigationArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LibraryItemsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _mediaType = MutableStateFlow(LibraryMediaType.MOVIE)

    private val libraryItemTypeString = savedStateHandle.getStateFlow(
        key = libraryItemTypeNavigationArgument,
        initialValue = ""
    )

    val libraryItemType: StateFlow<LibraryItemType?> = libraryItemTypeString
        .map {
            if (it.isEmpty()) null
            else enumValueOf<LibraryItemType>(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val movieItems: StateFlow<List<LibraryItem>> = libraryItemTypeString
        .flatMapLatest { itemTypeString ->
            if (itemTypeString.isEmpty()) {
                flow { }
            } else {
                val itemType = enumValueOf<LibraryItemType>(itemTypeString)
                when (itemType) {
                    LibraryItemType.FAVORITE -> libraryRepository.favoriteMovies
                    LibraryItemType.WATCHLIST -> libraryRepository.moviesWatchlist
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val tvItems: StateFlow<List<LibraryItem>> = libraryItemTypeString
        .flatMapLatest { itemTypeString ->
            if (itemTypeString.isEmpty()) {
                flow { }
            } else {
                val itemType = enumValueOf<LibraryItemType>(itemTypeString)
                when (itemType) {
                    LibraryItemType.FAVORITE -> libraryRepository.favoriteTvShows
                    LibraryItemType.WATCHLIST -> libraryRepository.tvShowsWatchlist
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun deleteItem(libraryItem: LibraryItem) {
        viewModelScope.launch {
            try {
                libraryRepository.addOrRemoveFromWatchlist(libraryItem)
            } catch (e: IOException) {
                _errorMessage.update { "An error occurred" }
            }
        }
    }

    fun onMediaTypeChange(mediaType: LibraryMediaType) {
        _mediaType.update { mediaType }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }
}