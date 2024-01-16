package com.anshtya.data.repository.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.anshtya.core.local.database.MovieInfoDatabase
import com.anshtya.core.local.database.entity.asModel
import com.anshtya.core.model.content.FreeContentType
import com.anshtya.core.model.content.PopularContentType
import com.anshtya.core.model.content.TrendingContentTimeWindow
import com.anshtya.core.model.content.ContentItem
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.mediator.FreeContentRemoteMediator
import com.anshtya.data.mediator.PopularContentRemoteMediator
import com.anshtya.data.mediator.TrendingMoviesRemoteMediator
import com.anshtya.data.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
internal class ContentRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val db: MovieInfoDatabase
) : ContentRepository {

    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        initialLoadSize = PAGE_SIZE,
        prefetchDistance = 2
    )

    override fun getFreeContent(
        contentType: FreeContentType,
        includeAdult: Boolean,
        shouldReload: Boolean
    ): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = FreeContentRemoteMediator(
                tmdbApi = tmdbApi,
                db = db,
                contentType = contentType,
                includeAdult = includeAdult,
                shouldReload = shouldReload
            ),
            pagingSourceFactory = { db.freeContentDao().pagingSource() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { freeContentEntity ->
                    freeContentEntity.asModel()
                }
            }
    }

    override fun getTrendingMovies(
        timeWindow: TrendingContentTimeWindow,
        shouldReload: Boolean
    ): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = TrendingMoviesRemoteMediator(
                tmdbApi = tmdbApi,
                db = db,
                timeWindow = timeWindow,
                shouldReload = shouldReload
            ),
            pagingSourceFactory = { db.trendingContentDao().pagingSource() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { trendingContentEntity ->
                    trendingContentEntity.asModel()
                }
            }
    }

    override fun getPopularContent(
        contentType: PopularContentType,
        includeAdult: Boolean,
        shouldReload: Boolean
    ): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = PopularContentRemoteMediator(
                tmdbApi = tmdbApi,
                db = db,
                contentType = contentType,
                includeAdult = includeAdult,
                shouldReload = shouldReload
            ),
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