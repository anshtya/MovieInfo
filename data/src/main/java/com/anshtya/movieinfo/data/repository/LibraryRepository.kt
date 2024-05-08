package com.anshtya.movieinfo.data.repository

import com.anshtya.movieinfo.core.model.library.LibraryItem
import com.anshtya.movieinfo.data.util.Synchronizer
import kotlinx.coroutines.flow.Flow

interface LibraryRepository: Synchronizer {
    val favoriteMovies: Flow<List<LibraryItem>>
    val favoriteTvShows: Flow<List<LibraryItem>>
    val moviesWatchlist: Flow<List<LibraryItem>>
    val tvShowsWatchlist: Flow<List<LibraryItem>>

    suspend fun favoriteItemExists(mediaId: Int): Boolean

    suspend fun itemInWatchlistExists(mediaId: Int): Boolean

    suspend fun addOrRemoveFavorite(libraryItem: LibraryItem): Boolean

    suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem): Boolean
}