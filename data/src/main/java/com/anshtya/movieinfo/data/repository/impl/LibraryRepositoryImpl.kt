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
import com.anshtya.movieinfo.data.repository.LibraryRepository
import com.anshtya.movieinfo.data.util.SyncManager
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
    private val movieMediaTypeString = MediaType.MOVIE.name.lowercase()
    private val tvMediaTypeString = MediaType.TV.name.lowercase()

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

    override suspend fun addOrRemoveFavorite(libraryItem: LibraryItem): Boolean {
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
                        mediaType = enumValueOf<MediaType>(libraryItem.mediaType.uppercase()),
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
                        mediaType = enumValueOf<MediaType>(libraryItem.mediaType.uppercase()),
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
        mediaType: MediaType,
        libraryItemType: LibraryItemType,
        itemExistsLocally: Boolean
    ): Boolean {
        val accountId = accountDetailsDao.getAccountDetails()?.id ?: return false
        return try {
            when (libraryItemType) {
                LibraryItemType.FAVORITE -> {
                    val favoriteRequest = FavoriteRequest(
                        mediaType = mediaType.name.lowercase(),
                        mediaId = id,
                        favorite = itemExistsLocally
                    )
                    tmdbApi.addOrRemoveFavorite(accountId, favoriteRequest)
                }

                LibraryItemType.WATCHLIST -> {
                    val watchlistRequest = WatchlistRequest(
                        mediaType = mediaType.name.lowercase(),
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

            val favoriteMovies = mutableListOf<FavoriteContentEntity>()
            val favoriteTvShows = mutableListOf<FavoriteContentEntity>()
            val staleFavoriteMovies = mutableListOf<Pair<Int, String>>()
            val staleFavoriteTvShows = mutableListOf<Pair<Int, String>>()

            val favoriteItemTypeString = LibraryItemType.FAVORITE.name.lowercase()

            coroutineScope {
                launch {
                    val favoriteMoviesResults = mutableListOf<NetworkContentItem>()

                    var movieFavoritesPage = 1
                    do {
                        val result = tmdbApi.getLibraryItems(
                            accountId = accountId,
                            itemType = favoriteItemTypeString,
                            mediaType = "${movieMediaTypeString}s",
                            page = movieFavoritesPage++
                        ).results

                        favoriteMoviesResults.addAll(result)
                    } while (result.isNotEmpty())

                    val favoriteMoviesResultsPair = favoriteMoviesResults
                        .map {
                            Pair(it.id, movieMediaTypeString)
                        }

                    staleFavoriteMovies.addAll(
                        favoriteContentDao.getFavoriteMovies()
                            .first()
                            .filter {
                                Pair(it.mediaId, it.mediaType) !in favoriteMoviesResultsPair
                                        && syncManager.isWorkNotScheduled(
                                    mediaId = it.mediaId,
                                    mediaType = MediaType.MOVIE,
                                    itemType = LibraryItemType.FAVORITE
                                )
                            }
                            .map { Pair(it.mediaId, it.mediaType) }
                    )

                    favoriteMovies.addAll(
                        favoriteMoviesResults
                            .filter {
                                syncManager.isWorkNotScheduled(
                                    mediaId = it.id,
                                    mediaType = MediaType.MOVIE,
                                    itemType = LibraryItemType.FAVORITE
                                )
                            }.map {
                                val contentItem = it.asModel()
                                val item = favoriteContentDao.getFavoriteItem(
                                    mediaId = contentItem.id,
                                    mediaType = movieMediaTypeString
                                )

                                item?.copy(
                                    imagePath = contentItem.imagePath,
                                    name = contentItem.name
                                ) ?: FavoriteContentEntity(
                                    mediaId = contentItem.id,
                                    mediaType = movieMediaTypeString,
                                    imagePath = contentItem.imagePath,
                                    name = contentItem.name
                                )
                            }
                    )
                }

                launch {
                    val favoriteTvShowsResults = mutableListOf<NetworkContentItem>()

                    var tvFavoritesPage = 1
                    do {
                        val result = tmdbApi.getLibraryItems(
                            accountId = accountId,
                            itemType = favoriteItemTypeString,
                            mediaType = tvMediaTypeString,
                            page = tvFavoritesPage++
                        ).results

                        favoriteTvShowsResults.addAll(result)
                    } while (result.isNotEmpty())

                    val favoriteTvShowsResultsPair = favoriteTvShowsResults
                        .map {
                            Pair(it.id, tvMediaTypeString)
                        }

                    staleFavoriteTvShows.addAll(
                        favoriteContentDao.getFavoriteTvShows()
                            .first()
                            .filter {
                                Pair(
                                    it.mediaId,
                                    it.mediaType
                                ) !in favoriteTvShowsResultsPair
                                        && syncManager.isWorkNotScheduled(
                                    mediaId = it.mediaId,
                                    mediaType = MediaType.TV,
                                    itemType = LibraryItemType.FAVORITE
                                )
                            }
                            .map { Pair(it.mediaId, it.mediaType) }
                    )

                    favoriteTvShows.addAll(
                        favoriteTvShowsResults
                            .filter {
                                syncManager.isWorkNotScheduled(
                                    mediaId = it.id,
                                    mediaType = MediaType.TV,
                                    itemType = LibraryItemType.FAVORITE
                                )
                            }
                            .map {
                                val contentItem = it.asModel()
                                val item = favoriteContentDao.getFavoriteItem(
                                    mediaId = contentItem.id,
                                    mediaType = tvMediaTypeString
                                )

                                item?.copy(
                                    imagePath = contentItem.imagePath,
                                    name = contentItem.name
                                ) ?: FavoriteContentEntity(
                                    mediaId = contentItem.id,
                                    mediaType = tvMediaTypeString,
                                    imagePath = contentItem.imagePath,
                                    name = contentItem.name
                                )
                            }
                    )
                }
            }

            favoriteContentDao.syncFavoriteItems(
                upsertItems = (favoriteMovies + favoriteTvShows),
                deleteItems = (staleFavoriteMovies + staleFavoriteTvShows)
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

            val moviesWatchlist = mutableListOf<WatchlistContentEntity>()
            val tvShowsWatchlist = mutableListOf<WatchlistContentEntity>()
            val staleMoviesWatchlist = mutableListOf<Pair<Int, String>>()
            val staleTvShowsWatchlist = mutableListOf<Pair<Int, String>>()

            val watchlistItemTypeString = LibraryItemType.WATCHLIST.name.lowercase()

            coroutineScope {
                launch {
                    val moviesWatchlistResults = mutableListOf<NetworkContentItem>()

                    var movieWatchlistPage = 1
                    do {
                        val result = tmdbApi.getLibraryItems(
                            accountId = accountId,
                            itemType = watchlistItemTypeString,
                            mediaType = "${movieMediaTypeString}s",
                            page = movieWatchlistPage++
                        ).results

                        moviesWatchlistResults.addAll(result)
                    } while (result.isNotEmpty())

                    val moviesWatchlistResultsPair = moviesWatchlistResults
                        .map {
                            Pair(it.id, movieMediaTypeString)
                        }

                    staleMoviesWatchlist.addAll(
                        watchlistContentDao.getMoviesWatchlist()
                            .first()
                            .filter {
                                Pair(
                                    it.mediaId,
                                    it.mediaType
                                ) !in moviesWatchlistResultsPair
                                        && syncManager.isWorkNotScheduled(
                                    mediaId = it.mediaId,
                                    mediaType = MediaType.MOVIE,
                                    itemType = LibraryItemType.WATCHLIST
                                )
                            }.map { Pair(it.mediaId, it.mediaType) }
                    )

                    moviesWatchlist.addAll(
                        moviesWatchlistResults
                            .filter {
                                syncManager.isWorkNotScheduled(
                                    mediaId = it.id,
                                    mediaType = MediaType.MOVIE,
                                    itemType = LibraryItemType.WATCHLIST
                                )
                            }
                            .map {
                                val contentItem = it.asModel()
                                val item = watchlistContentDao.getWatchlistItem(
                                    mediaId = contentItem.id,
                                    mediaType = movieMediaTypeString
                                )

                                item?.copy(
                                    imagePath = contentItem.imagePath,
                                    name = contentItem.name
                                ) ?: WatchlistContentEntity(
                                    mediaId = contentItem.id,
                                    mediaType = movieMediaTypeString,
                                    imagePath = contentItem.imagePath,
                                    name = contentItem.name
                                )
                            }
                    )
                }

                launch {
                    val tvShowsWatchlistResults = mutableListOf<NetworkContentItem>()

                    var tvWatchlistPage = 1
                    do {
                        val result = tmdbApi.getLibraryItems(
                            accountId = accountId,
                            itemType = watchlistItemTypeString,
                            mediaType = tvMediaTypeString,
                            page = tvWatchlistPage++
                        ).results

                        tvShowsWatchlistResults.addAll(result)
                    } while (result.isNotEmpty())

                    val tvShowsWatchlistResultsPair = tvShowsWatchlistResults
                        .map {
                            Pair(it.id, tvMediaTypeString)
                        }

                    staleTvShowsWatchlist.addAll(
                        watchlistContentDao.getTvShowsWatchlist()
                            .first()
                            .filter {
                                Pair(
                                    it.mediaId,
                                    it.mediaType
                                ) !in tvShowsWatchlistResultsPair
                                        && syncManager.isWorkNotScheduled(
                                    mediaId = it.mediaId,
                                    mediaType = MediaType.TV,
                                    itemType = LibraryItemType.WATCHLIST
                                )
                            }
                            .map { Pair(it.mediaId, it.mediaType) }
                    )

                    tvShowsWatchlist.addAll(
                        tvShowsWatchlistResults
                            .filter {
                                syncManager.isWorkNotScheduled(
                                    mediaId = it.id,
                                    mediaType = MediaType.TV,
                                    itemType = LibraryItemType.WATCHLIST
                                )
                            }
                            .map {
                                val contentItem = it.asModel()
                                val item = watchlistContentDao.getWatchlistItem(
                                    mediaId = contentItem.id,
                                    mediaType = tvMediaTypeString
                                )

                                item?.copy(
                                    imagePath = contentItem.imagePath,
                                    name = contentItem.name
                                ) ?: WatchlistContentEntity(
                                    mediaId = contentItem.id,
                                    mediaType = tvMediaTypeString,
                                    imagePath = contentItem.imagePath,
                                    name = contentItem.name
                                )
                            }
                    )
                }
            }

            watchlistContentDao.syncWatchlistItems(
                upsertItems = (moviesWatchlist + tvShowsWatchlist),
                deleteItems = (staleMoviesWatchlist + staleTvShowsWatchlist)
            )

            true
        } catch (e: IOException) {
            false
        } catch (e: HttpException) {
            false
        }
    }
}