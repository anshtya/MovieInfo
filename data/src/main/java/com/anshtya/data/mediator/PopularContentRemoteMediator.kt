package com.anshtya.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.anshtya.core.local.database.MovieInfoDatabase
import com.anshtya.core.local.database.entity.EntityLastModified
import com.anshtya.core.local.database.entity.PopularContentEntity
import com.anshtya.core.local.database.entity.PopularContentRemoteKey
import com.anshtya.core.model.content.PopularContentType
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.model.asPopularContentEntity
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
internal class PopularContentRemoteMediator(
    private val tmdbApi: TmdbApi,
    private val db: MovieInfoDatabase,
    private val contentType: PopularContentType,
    private val includeAdult: Boolean,
    private val shouldReload: Boolean
) : RemoteMediator<Int, PopularContentEntity>() {
    private val entityName = db.popularContentEntityName
    private val lastModifiedDao = db.entityLastModifiedDao()
    private val popularContentDao = db.popularContentDao()
    private val remoteKeyDao = db.popularContentRemoteKeyDao()

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
                PopularContentType.STREAMING -> tmdbApi.getPopularStreamingTitles(
                    page = page,
                    includeAdult = includeAdult
                )

                PopularContentType.IN_THEATRES -> tmdbApi.getPopularTitlesInTheatres(
                    page = page,
                    includeAdult = includeAdult
                )

                PopularContentType.FOR_RENT -> tmdbApi.getPopularTitlesOnRent(
                    page = page,
                    includeAdult = includeAdult
                )
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
        state: PagingState<Int, PopularContentEntity>
    ): PopularContentRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { popularContentEntity ->
                remoteKeyDao.remoteKeyByQuery(popularContentEntity.remoteId)
            }
    }
}