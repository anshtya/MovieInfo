package com.anshtya.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.anshtya.data.model.asFreeContentEntity
import com.anshtya.local.database.MovieInfoDatabase
import com.anshtya.local.database.entity.FreeContentEntity
import com.anshtya.local.database.entity.FreeContentRemoteKey
import com.anshtya.local.datastore.ContentPreferencesDataStore
import com.anshtya.network.retrofit.TmdbApi
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class FreeContentRemoteMediator @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val db: MovieInfoDatabase,
    private val dataStore: ContentPreferencesDataStore,
    private val contentType: String
) : RemoteMediator<Int, FreeContentEntity>() {
    private val freeContentDao = db.freeContentDao()
    private val remoteKeyDao = db.freeContentRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (
            dataStore.freeContentFilterString.first() == contentType
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
        state: PagingState<Int, FreeContentEntity>
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
            val response = tmdbApi.getFreeContent(contentType, page)
            val currentPage = response.page
            val totalPages = response.totalPages
            val endOfPaginationReached = currentPage == totalPages
            val nextPage = if (endOfPaginationReached) null else page + 1

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    freeContentDao.clearAll()
                    remoteKeyDao.clearAll()
                }

                val entities = response.results.map { it.asFreeContentEntity() }
                val keys = response.results.map {
                    FreeContentRemoteKey(
                        id = it.id,
                        nextKey = nextPage
                    )
                }
                freeContentDao.insertAll(entities)
                remoteKeyDao.insert(keys)

                dataStore.setFreeContentFilterString(contentType)
                dataStore.setDbLastUpdateTime(System.currentTimeMillis())
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, FreeContentEntity>
    ): FreeContentRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { freeContentEntity ->
                remoteKeyDao.remoteKeyByQuery(freeContentEntity.remoteId)
            }
    }
}