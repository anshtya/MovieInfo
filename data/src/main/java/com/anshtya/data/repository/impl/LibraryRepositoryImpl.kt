package com.anshtya.data.repository.impl

import com.anshtya.core.local.database.dao.FavoriteContentDao
import com.anshtya.core.local.database.dao.WatchlistContentDao
import com.anshtya.core.local.database.entity.FavoriteContentEntity
import com.anshtya.core.local.database.entity.WatchlistContentEntity
import com.anshtya.core.local.database.entity.asFavoriteContentEntity
import com.anshtya.core.local.database.entity.asModel
import com.anshtya.core.local.database.entity.asWatchlistContentEntity
import com.anshtya.core.local.datastore.UserPreferencesDataStore
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.core.model.library.LibraryTaskType
import com.anshtya.core.network.model.content.NetworkContentItem
import com.anshtya.core.network.model.library.FavoriteRequest
import com.anshtya.core.network.model.library.WatchlistRequest
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.model.asFavoriteContentEntity
import com.anshtya.data.model.asWatchlistContentEntity
import com.anshtya.data.repository.LibraryRepository
import com.anshtya.data.repository.util.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val favoriteContentDao: FavoriteContentDao,
    private val watchlistContentDao: WatchlistContentDao,
    private val userPreferencesDataStore: UserPreferencesDataStore,
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

    override suspend fun addOrRemoveFavorite(libraryItem: LibraryItem): Boolean {
        val favoriteContentEntity = libraryItem.asFavoriteContentEntity()
        val itemExists = favoriteContentDao.checkFavoriteItemExists(libraryItem.id)

        try {
            if (itemExists) {
                favoriteContentDao.deleteFavoriteItem(favoriteContentEntity)
            } else {
                favoriteContentDao.insertFavoriteItem(favoriteContentEntity)
            }
            return itemExists
        } catch (e: IOException) {
            throw e
        }
    }

    override suspend fun addOrRemoveFromWatchlist(libraryItem: LibraryItem): Boolean {
        val watchlistContentEntity = libraryItem.asWatchlistContentEntity()
        val itemExists = watchlistContentDao.checkWatchlistItemExists(libraryItem.id)

        try {
            if (itemExists) {
                watchlistContentDao.deleteWatchlistItem(watchlistContentEntity)
            } else {
                watchlistContentDao.insertWatchlistItem(watchlistContentEntity)
            }
            return itemExists
        } catch (e: IOException) {
            throw e
        }
    }

    override suspend fun addOrRemoveItemSync(
        id: Int,
        mediaType: String,
        libraryTaskType: LibraryTaskType,
        itemExistsLocally: Boolean
    ): Boolean {
        val accountId = userPreferencesDataStore.userData.first().accountDetails.id

        return try {
            when (libraryTaskType) {
                LibraryTaskType.FAVORITES -> {
                    val favoriteRequest = FavoriteRequest(
                        mediaType = mediaType,
                        mediaId = id,
                        favorite = itemExistsLocally
                    )
                    tmdbApi.addOrRemoveFavorite(accountId, favoriteRequest)
                }

                LibraryTaskType.WATCHLIST -> {
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
     * This function inserts items from server to database for which no work is scheduled
     * and removes items which are stale (i.e not present on server) and for which no work is
     * scheduled.
     */
    override suspend fun syncLibrary(): Boolean {
        return try {
            val accountId = userPreferencesDataStore.userData.first().accountDetails.id

            val favoriteMovies = tmdbApi.getFavoriteMovies(accountId).results
                .filter { syncManager.isWorkNotScheduled(it.id, LibraryTaskType.FAVORITES) }

            val favoriteTvShows = tmdbApi.getFavoriteTvShows(accountId).results
                .filter { syncManager.isWorkNotScheduled(it.id, LibraryTaskType.FAVORITES) }

            val favoriteItems = favoriteMovies + favoriteTvShows
            val favoriteItemsIds = favoriteItems.map { it.id }

            val staleFavoriteItems = favoriteContentDao.getFavoriteItems()
                .filter {
                    (it.id !in favoriteItemsIds) && syncManager.isWorkNotScheduled(
                        it.id,
                        LibraryTaskType.FAVORITES
                    )
                }
                .map { it.id }
            favoriteContentDao.syncItems(
                upsertItems = favoriteItems.map(NetworkContentItem::asFavoriteContentEntity),
                deleteItems = staleFavoriteItems
            )

            val moviesWatchlist = tmdbApi.getMoviesWatchlist(accountId).results
                .filter { syncManager.isWorkNotScheduled(it.id, LibraryTaskType.WATCHLIST) }

            val tvShowsWatchlist = tmdbApi.getTvShowsWatchlist(accountId).results
                .filter { syncManager.isWorkNotScheduled(it.id, LibraryTaskType.WATCHLIST) }

            val watchlistItems = moviesWatchlist + tvShowsWatchlist
            val watchlistItemsIds = watchlistItems.map { it.id }

            val staleWatchlistItems = watchlistContentDao.getWatchlistItems()
                .filter {
                    (it.id !in watchlistItemsIds) && syncManager.isWorkNotScheduled(
                        it.id,
                        LibraryTaskType.WATCHLIST
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