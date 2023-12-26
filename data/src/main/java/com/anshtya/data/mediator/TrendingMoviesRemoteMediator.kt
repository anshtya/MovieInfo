package com.anshtya.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.anshtya.data.model.asTrendingContentEntity
import com.anshtya.local.database.MovieInfoDatabase
import com.anshtya.local.database.entity.TrendingContentEntity
import com.anshtya.local.database.entity.TrendingContentRemoteKey
import com.anshtya.local.datastore.UserPreferencesDataStore
import com.anshtya.network.retrofit.TmdbApi
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
internal class TrendingMoviesRemoteMediator(
    private val tmdbApi: TmdbApi,
    private val db: MovieInfoDatabase,
    private val dataStore: UserPreferencesDataStore,
    private val timeWindow: String
) : RemoteMediator<Int, TrendingContentEntity>() {
    private val trendingContentDao = db.trendingContentDao()
    private val remoteKeyDao = db.trendingContentRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (
            dataStore.trendingContentFilterString.first() == timeWindow
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
        state: PagingState<Int, TrendingContentEntity>
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
            val response = tmdbApi.getTrendingMovies(timeWindow, page)
            val currentPage = response.page
            val totalPages = response.totalPages
            val endOfPaginationReached = currentPage == totalPages
            val nextPage = if (endOfPaginationReached) null else page + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    trendingContentDao.clearAll()
                    remoteKeyDao.clearAll()
                }

                val entities = response.results.map { it.asTrendingContentEntity() }
                val keys = response.results.map {
                    TrendingContentRemoteKey(
                        id = it.id,
                        nextKey = nextPage
                    )
                }
                trendingContentDao.insertAll(entities)
                remoteKeyDao.insert(keys)

                dataStore.setTrendingContentFilterString(timeWindow)
                dataStore.setDbLastUpdateTime(System.currentTimeMillis())
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, TrendingContentEntity>
    ): TrendingContentRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { trendingContentEntity ->
                remoteKeyDao.remoteKeyByQuery(trendingContentEntity.remoteId)
            }
    }
}