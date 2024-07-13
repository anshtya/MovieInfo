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

    val movieLibrary = testLibraryItems.filter { it.mediaType == movieMediaType }
    val tvLibrary = testLibraryItems.filter { it.mediaType == tvMediaType }

    private val _favoriteMovies = MutableStateFlow(movieLibrary)
    private val _favoriteTvShows = MutableStateFlow(tvLibrary)
    private val _moviesWatchlist = MutableStateFlow(movieLibrary)
    private val _tvShowsWatchlist = MutableStateFlow(tvLibrary)

    override val favoriteMovies: Flow<List<LibraryItem>> = _favoriteMovies.asStateFlow()

    override val favoriteTvShows: Flow<List<LibraryItem>> = _favoriteTvShows.asStateFlow()

    override val moviesWatchlist: Flow<List<LibraryItem>> = _moviesWatchlist.asStateFlow()

    override val tvShowsWatchlist: Flow<List<LibraryItem>> = _tvShowsWatchlist.asStateFlow()

    override suspend fun favoriteItemExists(mediaId: Int): Boolean {
        return testLibraryItems.find { it.id == mediaId } != null
    }

    override suspend fun itemInWatchlistExists(mediaId: Int): Boolean {
        return testLibraryItems.find { it.id == mediaId } != null
    }

    override suspend fun addOrRemoveFavorite(libraryItem: LibraryItem): Boolean {
        return if (generateError) {
            throw IOException()
        } else {
            // For testing add
            if (libraryItem.id == 1) return true

            // For testing delete
            when (enumValueOf<MediaType>(libraryItem.mediaType.uppercase())) {
                MediaType.MOVIE -> _favoriteMovies.update {
                    removeFromMovieLibrary(libraryItem)
                }

                MediaType.TV -> _favoriteTvShows.update {
                    removeFromTvShowLibrary(libraryItem)
                }

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
            if (libraryItem.id == 1) return true

            // For testing delete
            when (enumValueOf<MediaType>(libraryItem.mediaType.uppercase())) {
                MediaType.MOVIE -> _moviesWatchlist.update {
                    removeFromMovieLibrary(libraryItem)
                }

                MediaType.TV -> _tvShowsWatchlist.update {
                    removeFromTvShowLibrary(libraryItem)
                }

                else -> {}
            }

            true
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

    private fun removeFromMovieLibrary(libraryItem: LibraryItem) =
        movieLibrary.filter { it.id != libraryItem.id }

    private fun removeFromTvShowLibrary(libraryItem: LibraryItem) =
        tvLibrary.filter { it.id != libraryItem.id }
}