package com.anshtya.movieinfo.core.testing.repository

import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.library.LibraryItem
import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.core.testing.util.testLibraryItems
import com.anshtya.movieinfo.core.testing.util.movieMediaType
import com.anshtya.movieinfo.core.testing.util.tvMediaType
import com.anshtya.movieinfo.data.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

class TestLibraryRepository : LibraryRepository {
    private var generateError = false

    private val movieLibrary = testLibraryItems.filter { it.mediaType == movieMediaType }
    private val tvLibrary = testLibraryItems.filter { it.mediaType == tvMediaType }

    private val _movies = MutableStateFlow(movieLibrary)
    private val _tvShows = MutableStateFlow(tvLibrary)

    override val favoriteMovies: Flow<List<LibraryItem>> = _movies.asStateFlow()

    override val favoriteTvShows: Flow<List<LibraryItem>> = _tvShows.asStateFlow()

    override val moviesWatchlist: Flow<List<LibraryItem>> = _movies.asStateFlow()

    override val tvShowsWatchlist: Flow<List<LibraryItem>> = _tvShows.asStateFlow()

    override suspend fun itemInFavoritesExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean {
        return testLibraryItems.find { it.id == mediaId } != null
    }

    override suspend fun itemInWatchlistExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean {
        return testLibraryItems.find { it.id == mediaId } != null
    }

    override suspend fun addOrRemoveFavorite(libraryItem: LibraryItem): Boolean {
        return if (generateError) {
            throw IOException()
        } else {
            // For testing add
            if (libraryItem.id == 0) return true

            // For testing delete
            // Since delete button is present on items list, list needs to be updated
            when (enumValueOf<MediaType>(libraryItem.mediaType.uppercase())) {
                MediaType.MOVIE -> _movies.update { it - libraryItem }

                MediaType.TV -> _tvShows.update { it - libraryItem }

                else -> {}
            }

            true
        }
    }

    override suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem): Boolean {
        return if (generateError) {
            throw IOException()
        } else {
            // For testing add
            if (libraryItem.id == 0) return true

            // For testing delete
            // Since delete button is present on items list, list needs to be updated
            when (enumValueOf<MediaType>(libraryItem.mediaType.uppercase())) {
                MediaType.MOVIE -> _movies.update { it - libraryItem }

                MediaType.TV -> _tvShows.update { it - libraryItem }

                else -> {}
            }

            true
        }
    }

    override suspend fun executeLibraryTask(
        id: Int,
        mediaType: MediaType,
        libraryItemType: LibraryItemType,
        itemExistsLocally: Boolean
    ): Boolean = true

    override suspend fun syncFavorites(): Boolean = true

    override suspend fun syncWatchlist(): Boolean = true

    fun generateError(value: Boolean) {
        generateError = value
    }
}