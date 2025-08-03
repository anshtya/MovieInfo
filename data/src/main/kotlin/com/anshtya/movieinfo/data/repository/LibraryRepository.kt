package com.anshtya.movieinfo.data.repository

import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.library.LibraryItem
import com.anshtya.movieinfo.data.repository.util.LibraryTaskSyncOperation
import com.anshtya.movieinfo.data.repository.util.UserLibrarySyncOperations
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