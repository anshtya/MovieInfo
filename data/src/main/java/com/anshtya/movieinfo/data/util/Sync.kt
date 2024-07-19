package com.anshtya.movieinfo.data.util

import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.library.LibraryItem
import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.core.model.library.LibraryTask
import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * Interface for class which schedules sync work.
 */
interface SyncScheduler {
    fun scheduleLibraryTaskWork(libraryTask: LibraryTask)

    fun scheduleLibrarySyncWork()

    fun isWorkNotScheduled(
        mediaId: Int,
        mediaType: MediaType,
        itemType: LibraryItemType
    ): Boolean
}

/**
 * Interface to add capability to execute operation of [LibraryTask] work enqueued by
 * [SyncScheduler] WorkManager.
 */
interface LibraryTaskSyncOperation {
    suspend fun executeLibraryTask(
        id: Int,
        mediaType: MediaType,
        libraryItemType: LibraryItemType,
        itemExistsLocally: Boolean
    ): Boolean
}

/**
 * Interface to provide a common implementation for synchronization of user library
 * between local and remote data source.
 */
interface Synchronizer {
    suspend fun syncFromLocalAndNetwork(
        fetchFromNetwork: suspend (
            mediaTypeString: String
        ) -> List<NetworkContentItem>,

        fetchStaleItemsFromLocalSource: suspend (
            mediaType: MediaType,
            networkResultsPair: List<Pair<Int, String>>
        ) -> List<Pair<Int, String>>,

        fetchFromLocalSource: suspend (
            mediaType: MediaType,
            mediaTypeString: String,
            networkResults: List<NetworkContentItem>
        ) -> List<LibraryItem>,

        updateLocalSource: suspend (
            libraryItems: List<LibraryItem>,
            staleItems: List<Pair<Int, String>>
        ) -> Unit
    ): Boolean {
        val movieMediaTypeString = MediaType.MOVIE.name.lowercase()
        val tvMediaTypeString = MediaType.TV.name.lowercase()

        val moviesLibrary = mutableListOf<LibraryItem>()
        val tvShowsLibrary = mutableListOf<LibraryItem>()
        val staleMovies = mutableListOf<Pair<Int, String>>()
        val staleTvShows = mutableListOf<Pair<Int, String>>()

        return try {
            coroutineScope {
                launch {
                    val moviesLibraryNetworkResults = mutableListOf<NetworkContentItem>()

                    moviesLibraryNetworkResults.addAll(
                        fetchFromNetwork("${movieMediaTypeString}s")
                    )

                    val moviesLibraryNetworkResultsPair = moviesLibraryNetworkResults
                        .map {
                            Pair(it.id, movieMediaTypeString)
                        }

                    staleMovies.addAll(
                        fetchStaleItemsFromLocalSource(
                            MediaType.MOVIE, moviesLibraryNetworkResultsPair
                        )
                    )

                    moviesLibrary.addAll(
                        fetchFromLocalSource(
                            MediaType.MOVIE, movieMediaTypeString, moviesLibraryNetworkResults
                        )
                    )
                }

                launch {
                    val tvShowsLibraryNetworkResults = mutableListOf<NetworkContentItem>()

                    tvShowsLibraryNetworkResults.addAll(
                        fetchFromNetwork(tvMediaTypeString)
                    )

                    val tvShowsLibraryNetworkResultsPair = tvShowsLibraryNetworkResults
                        .map {
                            Pair(it.id, tvMediaTypeString)
                        }

                    staleTvShows.addAll(
                        fetchStaleItemsFromLocalSource(
                            MediaType.TV, tvShowsLibraryNetworkResultsPair
                        )
                    )

                    tvShowsLibrary.addAll(
                        fetchFromLocalSource(
                            MediaType.TV, tvMediaTypeString, tvShowsLibraryNetworkResults
                        )
                    )
                }
            }

            updateLocalSource(
                (moviesLibrary + tvShowsLibrary),
                (staleMovies + staleTvShows)
            )

            return true
        } catch (e: IOException) {
            false
        } catch (e: HttpException) {
            false
        }
    }
}

/**
 * Interface to add user library sync capabilities to a class to manage synchronization
 * between local and remote data source.
 */
interface UserLibrarySyncOperations : Synchronizer {
    suspend fun syncFavorites(): Boolean

    suspend fun syncWatchlist(): Boolean
}