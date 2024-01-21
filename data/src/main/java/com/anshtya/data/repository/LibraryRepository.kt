package com.anshtya.data.repository

import com.anshtya.core.model.library.LibraryItem
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    val favoriteMovies: Flow<List<LibraryItem>>
    val favoriteTvShows: Flow<List<LibraryItem>>

    suspend fun addOrRemoveFavorites(libraryItem: LibraryItem)
    suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem)
}