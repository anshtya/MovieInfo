package com.anshtya.data.repository

import com.anshtya.core.model.library.LibraryItem
import com.anshtya.data.repository.util.Synchronizer
import kotlinx.coroutines.flow.Flow

interface LibraryRepository: Synchronizer {
    val favoriteMovies: Flow<List<LibraryItem>>
    val favoriteTvShows: Flow<List<LibraryItem>>
    val moviesWatchlist: Flow<List<LibraryItem>>
    val tvShowsWatchlist: Flow<List<LibraryItem>>

    suspend fun addOrRemoveFavorite(libraryItem: LibraryItem): Boolean
    suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem): Boolean
}