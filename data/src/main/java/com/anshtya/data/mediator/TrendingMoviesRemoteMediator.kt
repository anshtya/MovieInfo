package com.anshtya.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.anshtya.core.local.database.MovieInfoDatabase
import com.anshtya.core.local.database.entity.EntityLastModified
import com.anshtya.core.local.database.entity.TrendingContentEntity
import com.anshtya.core.local.database.entity.TrendingContentRemoteKey
import com.anshtya.core.model.TrendingContentTimeWindow
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.model.asTrendingContentEntity
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
internal class TrendingMoviesRemoteMediator(
    private val tmdbApi: TmdbApi,
    private val db: MovieInfoDatabase,
    private val timeWindow: TrendingContentTimeWindow,
    private val shouldReload: Boolean
) : RemoteMediator<Int, TrendingContentEntity>() {
    private val entityName = db.trendingContentEntityName
    private val lastModifiedDao = db.entityLastModifiedDao()
    private val trendingContentDao = db.trendingContentDao()
    private val remoteKeyDao = db.trendingContentRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastModified = lastModifiedDao.entityLastModified(entityName)
        return if (shouldReload || System.currentTimeMillis() - lastModified >= cacheTimeout) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
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
            val response = tmdbApi.getTrendingMovies(
                timeWindow = timeWindow.parameter,
                page = page
            )
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

                val lastModifiedEntity = EntityLastModified(
                    name = entityName,
                    lastModified = System.currentTimeMillis()
                )
                lastModifiedDao.upsertLastModifiedTime(lastModifiedEntity)
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