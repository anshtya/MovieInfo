package com.anshtya.movieinfo.core.testing.repository

import com.anshtya.movieinfo.core.model.library.LibraryItem
import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.core.testing.util.testLibraryItems
import com.anshtya.movieinfo.core.testing.util.movieMediaType
import com.anshtya.movieinfo.core.testing.util.tvMediaType
import com.anshtya.movieinfo.data.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class TestLibraryRepository : LibraryRepository {
    private var generateError = false

    private val movieLibrary = testLibraryItems.filter { it.mediaType == movieMediaType }
    private val tvLibrary = testLibraryItems.filter { it.mediaType == tvMediaType }

    override val favoriteMovies: Flow<List<LibraryItem>> = flow { emit(movieLibrary) }

    override val favoriteTvShows: Flow<List<LibraryItem>> = flow { emit(tvLibrary) }

    override val moviesWatchlist: Flow<List<LibraryItem>> = flow { emit(movieLibrary) }

    override val tvShowsWatchlist: Flow<List<LibraryItem>> = flow { emit(tvLibrary) }

    override suspend fun favoriteItemExists(mediaId: Int): Boolean {
        return testLibraryItems.find { it.id == mediaId } != null
    }

    override suspend fun itemInWatchlistExists(mediaId: Int): Boolean {
        return testLibraryItems.find { it.id == mediaId } != null
    }

    override suspend fun addOrRemoveFavorite(libraryItem: LibraryItem): Boolean {
        return if (!generateError) {
            true
        } else {
            throw IOException()
        }
    }

    override suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem): Boolean {
        return if (!generateError) {
            true
        } else {
            throw IOException()
        }
    }

    override suspend fun addOrRemoveItemSync(
        id: Int,
        mediaType: String,
        libraryItemType: LibraryItemType,
        itemExistsLocally: Boolean
    ): Boolean = true

    override suspend fun syncFavorites(): Boolean = true

    override suspend fun syncWatchlist(): Boolean = true

    fun generateError(value: Boolean) {
        generateError = value
    }
}