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
import com.anshtya.network.retrofit.TmdbApi
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FreeContentRemoteMediator @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val db: MovieInfoDatabase,
    private val contentType: String
) : RemoteMediator<Int, FreeContentEntity>() {
    private val freeContentDao = db.freeContentDao()
    private val remoteKeyDao = db.freeContentRemoteKeyDao()

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