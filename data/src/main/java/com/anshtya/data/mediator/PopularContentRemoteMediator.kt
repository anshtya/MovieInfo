package com.anshtya.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.anshtya.data.model.PopularContentType
import com.anshtya.data.model.asPopularContentEntity
import com.anshtya.local.database.MovieInfoDatabase
import com.anshtya.local.database.entity.PopularContentEntity
import com.anshtya.local.database.entity.PopularContentRemoteKey
import com.anshtya.local.datastore.UserPreferencesDataStore
import com.anshtya.network.retrofit.TmdbApi
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
internal class PopularContentRemoteMediator(
    private val tmdbApi: TmdbApi,
    private val db: MovieInfoDatabase,
    private val dataStore: UserPreferencesDataStore,
    private val contentType: PopularContentType
) : RemoteMediator<Int, PopularContentEntity>() {
    private val popularContentDao = db.popularContentDao()
    private val remoteKeyDao = db.popularContentRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (
            dataStore.popularContentFilterString.first() == contentType.name
            && System.currentTimeMillis() - dataStore.dbLastUpdateTime.first() <= cacheTimeout
        ) {
            // Cached data is up-to-date, so there is no need to re-fetch from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularContentEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                    remoteKey?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKey != null
                    )
                }
            }
            val response = when (contentType) {
                PopularContentType.STREAMING -> tmdbApi.getPopularStreamingTitles(page)
                PopularContentType.IN_THEATRES -> tmdbApi.getPopularTitlesInTheatres(page)
                PopularContentType.FOR_RENT -> tmdbApi.getPopularTitlesOnRent(page)
            }
            val currentPage = response.page
            val totalPages = response.totalPages
            val endOfPaginationReached = currentPage == totalPages
            val nextPage = if (endOfPaginationReached) null else page + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    popularContentDao.clearAll()
                    remoteKeyDao.clearAll()
                }

                val entities = response.results.map { it.asPopularContentEntity() }
                val keys = response.results.map {
                    PopularContentRemoteKey(
                        id = it.id,
                        nextKey = nextPage
                    )
                }
                popularContentDao.insertAll(entities)
                remoteKeyDao.insert(keys)

                dataStore.setPopularContentFilterString(contentType.name)
                dataStore.setDbLastUpdateTime(System.currentTimeMillis())
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, PopularContentEntity>
    ): PopularContentRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { popularContentEntity ->
                remoteKeyDao.remoteKeyByQuery(popularContentEntity.remoteId)
            }
    }
}