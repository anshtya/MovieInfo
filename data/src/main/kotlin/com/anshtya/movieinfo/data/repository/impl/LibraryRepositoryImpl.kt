package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.core.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.core.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.core.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.core.database.entity.FavoriteContentEntity
import com.anshtya.movieinfo.core.database.entity.WatchlistContentEntity
import com.anshtya.movieinfo.core.network.datasource.TmdbNetworkDataSource
import com.anshtya.movieinfo.core.network.model.NetworkResult
import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.core.network.model.library.FavoriteRequest
import com.anshtya.movieinfo.core.network.model.library.WatchlistRequest
import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.content.asModel
import com.anshtya.movieinfo.data.model.library.LibraryItem
import com.anshtya.movieinfo.data.model.library.LibraryItemType
import com.anshtya.movieinfo.data.model.library.LibraryTask
import com.anshtya.movieinfo.data.model.library.asFavoriteContentEntity
import com.anshtya.movieinfo.data.model.library.asLibraryItem
import com.anshtya.movieinfo.data.model.library.asWatchlistContentEntity
import com.anshtya.movieinfo.data.repository.LibraryRepository
import com.anshtya.movieinfo.data.repository.util.SyncScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

internal class LibraryRepositoryImpl @Inject constructor(
    private val networkDataSource: TmdbNetworkDataSource,
    private val favoriteContentDao: FavoriteContentDao,
    private val watchlistContentDao: WatchlistContentDao,
    private val accountDetailsDao: AccountDetailsDao,
    private val syncScheduler: SyncScheduler
) : LibraryRepository {
    override val favoriteMovies: Flow<List<LibraryItem>> =
        favoriteContentDao.getFavoriteMovies()
            .map { it.map(FavoriteContentEntity::asLibraryItem) }

    override val favoriteTvShows: Flow<List<LibraryItem>> =
        favoriteContentDao.getFavoriteTvShows()
            .map { it.map(FavoriteContentEntity::asLibraryItem) }

    override val moviesWatchlist: Flow<List<LibraryItem>> =
        watchlistContentDao.getMoviesWatchlist()
            .map { it.map(WatchlistContentEntity::asLibraryItem) }

    override val tvShowsWatchlist: Flow<List<LibraryItem>> =
        watchlistContentDao.getTvShowsWatchlist()
            .map { it.map(WatchlistContentEntity::asLibraryItem) }

    override suspend fun itemInFavoritesExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean {
        return favoriteContentDao.checkFavoriteItemExists(
            mediaId = mediaId,
            mediaType = mediaType.name.lowercase()
        )
    }

    override suspend fun itemInWatchlistExists(
        mediaId: Int,
        mediaType: MediaType
    ): Boolean {
        return watchlistContentDao.checkWatchlistItemExists(
            mediaId = mediaId,
            mediaType = mediaType.name.lowercase()
        )
    }

    override suspend fun addOrRemoveFavorite(libraryItem: LibraryItem) {
        try {
            val itemExists = favoriteContentDao.checkFavoriteItemExists(
                mediaId = libraryItem.id,
                mediaType = libraryItem.mediaType
            )
            if (itemExists) {
                favoriteContentDao.deleteFavoriteItem(
                    mediaId = libraryItem.id,
                    mediaType = libraryItem.mediaType
                )
            } else {
                favoriteContentDao.insertFavoriteItem(libraryItem.asFavoriteContentEntity())
            }

            val libraryTask = LibraryTask.favoriteItemTask(
                mediaId = libraryItem.id,
                mediaType = enumValueOf<MediaType>(libraryItem.mediaType.uppercase()),
                itemExists = !itemExists
            )
            syncScheduler.scheduleLibraryTaskWork(libraryTask)
        } catch (e: IOException) {
            throw e
        }
    }

    override suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem) {
        try {
            val itemExists = watchlistContentDao.checkWatchlistItemExists(
                mediaId = libraryItem.id,
                mediaType = libraryItem.mediaType
            )
            if (itemExists) {
                watchlistContentDao.deleteWatchlistItem(
                    mediaId = libraryItem.id,
                    mediaType = libraryItem.mediaType
                )
            } else {
                watchlistContentDao.insertWatchlistItem(libraryItem.asWatchlistContentEntity())
            }

            val libraryTask = LibraryTask.watchlistItemTask(
                mediaId = libraryItem.id,
                mediaType = enumValueOf<MediaType>(libraryItem.mediaType.uppercase()),
                itemExists = !itemExists
            )
            syncScheduler.scheduleLibraryTaskWork(libraryTask)
        } catch (e: IOException) {
            throw e
        }
    }

    override suspend fun executeLibraryTask(
        id: Int,
        mediaType: MediaType,
        libraryItemType: LibraryItemType,
        itemExistsLocally: Boolean
    ): Boolean {
        val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false
        val result = when (libraryItemType) {
            LibraryItemType.FAVORITE -> {
                val favoriteRequest = FavoriteRequest(
                    mediaType = mediaType.name.lowercase(),
                    mediaId = id,
                    favorite = itemExistsLocally
                )
                networkDataSource.addOrRemoveFavorite(accountId, favoriteRequest)
            }

            LibraryItemType.WATCHLIST -> {
                val watchlistRequest = WatchlistRequest(
                    mediaType = mediaType.name.lowercase(),
                    mediaId = id,
                    watchlist = itemExistsLocally
                )
                networkDataSource.addOrRemoveFromWatchlist(accountId, watchlistRequest)
            }
        }
        return result is NetworkResult.Success
    }

    /**
     * This function syncs favorites from server by inserting items into database and removes
     * items which are stale (i.e. not present on server) and for which no work is scheduled.
     */
    override suspend fun syncFavorites(): Boolean {
        val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false

        val favoriteItemTypeString = LibraryItemType.FAVORITE.name.lowercase()
        return syncFromLocalAndNetwork(
            fetchFromNetwork = { mediaTypeString ->
                val favoriteItemsNetworkResults = mutableListOf<NetworkContentItem>()

                var favoriteItemsPage = 1
                do {
                    val result = when (
                        val response = networkDataSource.getLibraryItems(
                            accountId = accountId,
                            itemType = favoriteItemTypeString,
                            mediaType = mediaTypeString,
                            page = favoriteItemsPage++
                        )
                    ) {
                        is NetworkResult.Success -> response.data.results
                        is NetworkResult.Failure.HttpError -> throw IOException(response.errorMessage)
                        is NetworkResult.Failure.Unknown -> throw IOException(response.exception)
                    }

                    favoriteItemsNetworkResults.addAll(result)
                } while (result.isNotEmpty())

                favoriteItemsNetworkResults
            },
            fetchStaleItemsFromLocalSource = { mediaType, networkResultsPair ->
                val favoriteItems = when (mediaType) {
                    MediaType.MOVIE -> favoriteContentDao.getFavoriteMovies().first()
                    MediaType.TV -> favoriteContentDao.getFavoriteTvShows().first()
                    else -> emptyList() // Unreachable
                }

                favoriteItems
                    .filter {
                        Pair(it.mediaId, it.mediaType) !in networkResultsPair
                                && syncScheduler.isWorkNotScheduled(
                            mediaId = it.mediaId,
                            mediaType = mediaType,
                            itemType = LibraryItemType.FAVORITE
                        )
                    }
                    .map { Pair(it.mediaId, it.mediaType) }
            },
            fetchFromLocalSource = { mediaType, mediaTypeString, networkResults ->
                networkResults
                    .filter {
                        syncScheduler.isWorkNotScheduled(
                            mediaId = it.id,
                            mediaType = mediaType,
                            itemType = LibraryItemType.FAVORITE
                        )
                    }.map {
                        val contentItem = it.asModel()
                        val item = favoriteContentDao.getFavoriteItem(
                            mediaId = contentItem.id,
                            mediaType = mediaTypeString
                        )?.asLibraryItem()

                        item?.copy(
                            imagePath = contentItem.imagePath,
                            name = contentItem.name
                        ) ?: LibraryItem(
                            id = contentItem.id,
                            mediaType = mediaTypeString,
                            imagePath = contentItem.imagePath,
                            name = contentItem.name
                        )
                    }
            },
            updateLocalSource = { libraryItems, staleItems ->
                favoriteContentDao.syncFavoriteItems(
                    upsertItems = libraryItems.map { item ->
                        item.asFavoriteContentEntity()
                    },
                    deleteItems = staleItems
                )
            }
        )
    }

    /**
     * This function syncs watchlist from server by inserting items into database and removes
     * items which are stale (i.e. not present on server) and for which no work is scheduled.
     */
    override suspend fun syncWatchlist(): Boolean {
        val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false

        val watchlistItemTypeString = LibraryItemType.WATCHLIST.name.lowercase()
        return syncFromLocalAndNetwork(
            fetchFromNetwork = { mediaTypeString ->
                val watchlistItemsNetworkResults = mutableListOf<NetworkContentItem>()

                var watchlistItemsPage = 1
                do {
                    val result = when (
                        val response = networkDataSource.getLibraryItems(
                            accountId = accountId,
                            itemType = watchlistItemTypeString,
                            mediaType = mediaTypeString,
                            page = watchlistItemsPage++
                        )
                    ) {
                        is NetworkResult.Success -> response.data.results
                        is NetworkResult.Failure.HttpError -> throw IOException(response.errorMessage)
                        is NetworkResult.Failure.Unknown -> throw IOException(response.exception)
                    }

                    watchlistItemsNetworkResults.addAll(result)
                } while (result.isNotEmpty())

                watchlistItemsNetworkResults
            },
            fetchStaleItemsFromLocalSource = { mediaType, networkResultsPair ->
                val watchlistItems = when (mediaType) {
                    MediaType.MOVIE -> watchlistContentDao.getMoviesWatchlist().first()
                    MediaType.TV -> watchlistContentDao.getTvShowsWatchlist().first()
                    else -> emptyList() // Unreachable
                }

                watchlistItems
                    .filter {
                        Pair(it.mediaId, it.mediaType) !in networkResultsPair
                                && syncScheduler.isWorkNotScheduled(
                            mediaId = it.mediaId,
                            mediaType = mediaType,
                            itemType = LibraryItemType.WATCHLIST
                        )
                    }
                    .map { Pair(it.mediaId, it.mediaType) }
            },
            fetchFromLocalSource = { mediaType, mediaTypeString, networkResults ->
                networkResults
                    .filter {
                        syncScheduler.isWorkNotScheduled(
                            mediaId = it.id,
                            mediaType = mediaType,
                            itemType = LibraryItemType.WATCHLIST
                        )
                    }.map {
                        val contentItem = it.asModel()
                        val item = watchlistContentDao.getWatchlistItem(
                            mediaId = contentItem.id,
                            mediaType = mediaTypeString
                        )?.asLibraryItem()

                        item?.copy(
                            imagePath = contentItem.imagePath,
                            name = contentItem.name
                        ) ?: LibraryItem(
                            id = contentItem.id,
                            mediaType = mediaTypeString,
                            imagePath = contentItem.imagePath,
                            name = contentItem.name
                        )
                    }
            },
            updateLocalSource = { libraryItems, staleItems ->
                watchlistContentDao.syncWatchlistItems(
                    upsertItems = libraryItems.map { item ->
                        item.asWatchlistContentEntity()
                    },
                    deleteItems = staleItems
                )
            }
        )
    }
}