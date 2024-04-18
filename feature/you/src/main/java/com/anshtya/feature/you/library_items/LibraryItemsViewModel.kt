package com.anshtya.feature.you.library_items

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.core.model.library.LibraryTask
import com.anshtya.core.ui.ErrorText
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.data.util.SyncManager
import com.anshtya.feature.you.R
import com.anshtya.feature.you.libraryItemTypeNavigationArgument
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
    private val libraryRepository: LibraryRepository,
    private val syncManager: SyncManager
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<ErrorText?>(null)
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
            try {
                when (itemType) {
                    LibraryItemType.FAVORITES -> {
                        val itemExists = libraryRepository.addOrRemoveFavorite(libraryItem)
                        val libraryTask = LibraryTask.favoriteItemTask(
                            mediaId = libraryItem.id,
                            mediaType = enumValueOf(libraryItem.mediaType),
                            itemExists = !itemExists
                        )
                        syncManager.scheduleLibraryTaskWork(libraryTask)
                    }

                    LibraryItemType.WATCHLIST -> {
                        val itemExists = libraryRepository.addOrRemoveFromWatchlist(libraryItem)
                        val libraryTask = LibraryTask.watchlistItemTask(
                            mediaId = libraryItem.id,
                            mediaType = enumValueOf(libraryItem.mediaType),
                            itemExists = !itemExists
                        )
                        syncManager.scheduleLibraryTaskWork(libraryTask)
                    }
                }
            } catch (e: IOException) {
                _errorMessage.update { ErrorText.StringResource(id = R.string.error_message) }
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