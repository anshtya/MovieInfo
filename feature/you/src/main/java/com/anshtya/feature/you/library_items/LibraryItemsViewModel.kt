package com.anshtya.feature.you.library_items

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.feature.you.LibraryItemType
import com.anshtya.feature.you.libraryItemTypeNavigationArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LibraryItemsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val libraryRepository: LibraryRepository
) : ViewModel() {
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
                    LibraryItemType.FAVORITES -> libraryRepository.favoriteMovies
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
                    LibraryItemType.FAVORITES -> libraryRepository.favoriteTvShows
                    LibraryItemType.WATCHLIST -> libraryRepository.tvShowsWatchlist
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun deleteItem(libraryItem: LibraryItem, itemType: LibraryItemType) {
        viewModelScope.launch {
            when (itemType) {
                LibraryItemType.FAVORITES -> libraryRepository.addOrRemoveFavorites(libraryItem)
                LibraryItemType.WATCHLIST -> libraryRepository.addOrRemoveFromWatchlist(libraryItem)
            }
        }
    }

    fun onMediaTypeChange(mediaType: LibraryMediaType) {
        _mediaType.update { mediaType }
    }
}