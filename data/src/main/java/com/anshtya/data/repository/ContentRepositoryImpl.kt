package com.anshtya.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.anshtya.data.mediator.FreeContentRemoteMediator
import com.anshtya.data.mediator.PopularContentRemoteMediator
import com.anshtya.data.mediator.TrendingMoviesRemoteMediator
import com.anshtya.data.model.FreeItem
import com.anshtya.data.model.PopularContentType
import com.anshtya.data.model.PopularItem
import com.anshtya.data.model.TrendingItem
import com.anshtya.data.model.asModel
import com.anshtya.local.database.MovieInfoDatabase
import com.anshtya.local.datastore.UserPreferencesDataStore
import com.anshtya.network.retrofit.TmdbApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
internal class ContentRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val db: MovieInfoDatabase,
    private val dataStore: UserPreferencesDataStore
) : ContentRepository {

    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        initialLoadSize = PAGE_SIZE,
        prefetchDistance = 2
    )

    override fun getFreeContent(contentType: String): Flow<PagingData<FreeItem>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = FreeContentRemoteMediator(tmdbApi, db, dataStore, contentType),
            pagingSourceFactory = { db.freeContentDao().pagingSource() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { freeContentEntity ->
                    freeContentEntity.asModel()
                }
            }
    }

    override fun getTrendingMovies(timeWindow: String): Flow<PagingData<TrendingItem>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = TrendingMoviesRemoteMediator(tmdbApi, db, dataStore, timeWindow),
            pagingSourceFactory = { db.trendingContentDao().pagingSource() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { trendingContentEntity ->
                    trendingContentEntity.asModel()
                }
            }
    }

    override fun getPopularContent(contentType: PopularContentType): Flow<PagingData<PopularItem>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = PopularContentRemoteMediator(tmdbApi, db, dataStore, contentType),
            pagingSourceFactory = { db.popularContentDao().pagingSource() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { popularContentEntity ->
                    popularContentEntity.asModel()
                }
            }
    }
}