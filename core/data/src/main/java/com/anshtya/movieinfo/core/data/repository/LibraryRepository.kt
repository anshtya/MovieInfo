package com.anshtya.movieinfo.core.data.repository

import com.anshtya.movieinfo.core.data.util.LibraryTaskSyncOperation
import com.anshtya.movieinfo.core.data.util.UserLibrarySyncOperations
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.library.LibraryItem
import kotlinx.coroutines.flow.Flow

interface LibraryRepository: UserLibrarySyncOperations, LibraryTaskSyncOperation {
    val favoriteMovies: Flow<List<LibraryItem>>
    val favoriteTvShows: Flow<List<LibraryItem>>
    val moviesWatchlist: Flow<List<LibraryItem>>
    val tvShowsWatchlist: Flow<List<LibraryItem>>

    suspend fun itemInFavoritesExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean

    suspend fun itemInWatchlistExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean

    suspend fun addOrRemoveFavorite(libraryItem: LibraryItem)

    suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem)
}