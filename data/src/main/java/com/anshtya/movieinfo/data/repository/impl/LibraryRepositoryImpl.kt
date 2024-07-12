package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.core.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.core.local.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.core.local.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.core.local.database.entity.FavoriteContentEntity
import com.anshtya.movieinfo.core.local.database.entity.WatchlistContentEntity
import com.anshtya.movieinfo.core.local.database.entity.asFavoriteContentEntity
import com.anshtya.movieinfo.core.local.database.entity.asWatchlistContentEntity
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.library.LibraryItem
import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.core.model.library.LibraryTask
import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.core.network.model.library.FavoriteRequest
import com.anshtya.movieinfo.core.network.model.library.WatchlistRequest
import com.anshtya.movieinfo.core.network.retrofit.TmdbApi
import com.anshtya.movieinfo.data.model.asFavoriteContentEntity
import com.anshtya.movieinfo.data.model.asWatchlistContentEntity
import com.anshtya.movieinfo.data.repository.LibraryRepository
import com.anshtya.movieinfo.data.util.SyncManager
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class LibraryRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val favoriteContentDao: FavoriteContentDao,
    private val watchlistContentDao: WatchlistContentDao,
    private val accountDetailsDao: AccountDetailsDao,
    private val syncManager: SyncManager
) : LibraryRepository {
    override val favoriteMovies: Flow<List<LibraryItem>> =
        favoriteContentDao.getFavoriteMovies()
            .map { it.map(FavoriteContentEntity::asModel) }

    override val favoriteTvShows: Flow<List<LibraryItem>> =
        favoriteContentDao.getFavoriteTvShows()
            .map { it.map(FavoriteContentEntity::asModel) }

    override val moviesWatchlist: Flow<List<LibraryItem>> =
        watchlistContentDao.getMoviesWatchlist()
            .map { it.map(WatchlistContentEntity::asModel) }

    override val tvShowsWatchlist: Flow<List<LibraryItem>> =
        watchlistContentDao.getTvShowsWatchlist()
            .map { it.map(WatchlistContentEntity::asModel) }

    override suspend fun favoriteItemExists(mediaId: Int): Boolean {
        return favoriteContentDao.checkFavoriteItemExists(mediaId)
    }

    override suspend fun itemInWatchlistExists(mediaId: Int): Boolean {
        return watchlistContentDao.checkWatchlistItemExists(mediaId)
    }

    override suspend fun addOrRemoveFavorite(libraryItem: LibraryItem): Boolean {
        val favoriteContentEntity = libraryItem.asFavoriteContentEntity()

        try {
            val itemExists = favoriteContentDao.checkFavoriteItemExists(libraryItem.id)
            if (itemExists) {
                favoriteContentDao.deleteFavoriteItem(favoriteContentEntity)
            } else {
                favoriteContentDao.insertFavoriteItem(favoriteContentEntity)
            }

            withContext(NonCancellable) {
                val accountId = accountDetailsDao.getAccountDetails()!!.id
                val favoriteRequest = FavoriteRequest(
                    mediaType = libraryItem.mediaType,
                    mediaId = libraryItem.id,
                    favorite = !itemExists
                )

                val response = tmdbApi.addOrRemoveFavorite(accountId, favoriteRequest)
                if (!response.isSuccessful) {
                    val libraryTask = LibraryTask.favoriteItemTask(
                        mediaId = libraryItem.id,
                        mediaType = libraryItem.mediaType,
                        itemExists = !itemExists
                    )
                    syncManager.scheduleLibraryTaskWork(libraryTask)
                }
            }

            return itemExists
        } catch (e: IOException) {
            throw e
        }
    }

    override suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem): Boolean {
        val watchlistContentEntity = libraryItem.asWatchlistContentEntity()

        try {
            val itemExists = watchlistContentDao.checkWatchlistItemExists(libraryItem.id)
            if (itemExists) {
                watchlistContentDao.deleteWatchlistItem(watchlistContentEntity)
            } else {
                watchlistContentDao.insertWatchlistItem(watchlistContentEntity)
            }

            withContext(NonCancellable) {
                val accountId = accountDetailsDao.getAccountDetails()!!.id
                val watchlistRequest = WatchlistRequest(
                    mediaType = libraryItem.mediaType,
                    mediaId = libraryItem.id,
                    watchlist = !itemExists
                )
                val response = tmdbApi.addOrRemoveFromWatchlist(accountId, watchlistRequest)
                if (!response.isSuccessful) {
                    val libraryTask = LibraryTask.watchlistItemTask(
                        mediaId = libraryItem.id,
                        mediaType = libraryItem.mediaType,
                        itemExists = !itemExists
                    )
                    syncManager.scheduleLibraryTaskWork(libraryTask)
                }
            }
            return itemExists
        } catch (e: IOException) {
            throw e
        }
    }

    override suspend fun addOrRemoveItemSync(
        id: Int,
        mediaType: String,
        libraryItemType: LibraryItemType,
        itemExistsLocally: Boolean
    ): Boolean {
        val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false
        return try {
            when (libraryItemType) {
                LibraryItemType.FAVORITE -> {
                    val favoriteRequest = FavoriteRequest(
                        mediaType = mediaType,
                        mediaId = id,
                        favorite = itemExistsLocally
                    )
                    tmdbApi.addOrRemoveFavorite(accountId, favoriteRequest)
                }

                LibraryItemType.WATCHLIST -> {
                    val watchlistRequest = WatchlistRequest(
                        mediaType = mediaType,
                        mediaId = id,
                        watchlist = itemExistsLocally
                    )
                    tmdbApi.addOrRemoveFromWatchlist(accountId, watchlistRequest)
                }
            }
            true
        } catch (e: IOException) {
            false
        } catch (e: HttpException) {
            false
        }
    }

    /**
     * This function syncs favorites from server by inserting items into database and removes
     * items which are stale (i.e. not present on server) and for which no work is scheduled.
     */
    override suspend fun syncFavorites(): Boolean {
        return try {
            val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false

            var movieFavoritesPage = 1
            var tvFavoritesPage = 1
            val favoriteMovies = mutableListOf<NetworkContentItem>()
            val favoriteTvShows = mutableListOf<NetworkContentItem>()

            coroutineScope {
                launch {
                    do {
                        val result = tmdbApi.getLibraryItems(
                            accountId = accountId,
                            itemType = LibraryItemType.FAVORITE.name.lowercase(),
                            mediaType = "${MediaType.MOVIE.name.lowercase()}s",
                            page = movieFavoritesPage++
                        ).results

                        favoriteMovies.addAll(result)
                    } while (result.isNotEmpty())
                }

                launch {
                    do {
                        val result = tmdbApi.getLibraryItems(
                            accountId = accountId,
                            itemType = LibraryItemType.FAVORITE.name.lowercase(),
                            mediaType = MediaType.TV.name.lowercase(),
                            page = tvFavoritesPage++
                        ).results

                        favoriteTvShows.addAll(result)
                    } while (result.isNotEmpty())
                }
            }

            val favoriteItems = (favoriteMovies + favoriteTvShows).filter {
                syncManager.isWorkNotScheduled(id = it.id, itemType = LibraryItemType.FAVORITE)
            }

            val favoriteItemsIds = favoriteItems.map { it.id }

            val staleFavoriteItems = favoriteContentDao.getFavoriteItems()
                .filter {
                    (it.id !in favoriteItemsIds) && syncManager.isWorkNotScheduled(
                        id = it.id,
                        itemType = LibraryItemType.FAVORITE
                    )
                }
                .map { it.id }
            favoriteContentDao.syncItems(
                upsertItems = favoriteItems.map(NetworkContentItem::asFavoriteContentEntity),
                deleteItems = staleFavoriteItems
            )

            true
        } catch (e: IOException) {
            false
        } catch (e: HttpException) {
            false
        }
    }

    /**
     * This function syncs watchlist from server by inserting items into database and removes
     * items which are stale (i.e. not present on server) and for which no work is scheduled.
     */
    override suspend fun syncWatchlist(): Boolean {
        return try {
            val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false

            var movieWatchlistPage = 1
            var tvWatchlistPage = 1
            val moviesWatchlist = mutableListOf<NetworkContentItem>()
            val tvShowsWatchlist = mutableListOf<NetworkContentItem>()

            coroutineScope {
                launch {
                    do {
                        val result = tmdbApi.getLibraryItems(
                            accountId = accountId,
                            itemType = LibraryItemType.WATCHLIST.name.lowercase(),
                            mediaType = "${MediaType.MOVIE.name.lowercase()}s",
                            page = movieWatchlistPage++
                        ).results

                        moviesWatchlist.addAll(result)
                    } while (result.isNotEmpty())
                }
                launch {
                    do {
                        val result = tmdbApi.getLibraryItems(
                            accountId = accountId,
                            itemType = LibraryItemType.WATCHLIST.name.lowercase(),
                            mediaType = MediaType.TV.name.lowercase(),
                            page = tvWatchlistPage++
                        ).results

                        tvShowsWatchlist.addAll(result)
                    } while (result.isNotEmpty())
                }
            }

            val watchlistItems = (moviesWatchlist + tvShowsWatchlist).filter {
                syncManager.isWorkNotScheduled(id = it.id, itemType = LibraryItemType.WATCHLIST)
            }
            val watchlistItemsIds = watchlistItems.map { it.id }

            val staleWatchlistItems = watchlistContentDao.getWatchlistItems()
                .filter {
                    (it.id !in watchlistItemsIds) && syncManager.isWorkNotScheduled(
                        id = it.id,
                        itemType = LibraryItemType.WATCHLIST
                    )
                }
                .map { it.id }
            watchlistContentDao.syncItems(
                upsertItems = watchlistItems.map(NetworkContentItem::asWatchlistContentEntity),
                deleteItems = staleWatchlistItems
            )

            true
        } catch (e: IOException) {
            false
        } catch (e: HttpException) {
            false
        }
    }
}