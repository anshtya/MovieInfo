package com.anshtya.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anshtya.core.model.content.ContentItem
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.paging.tv.AiringTodayTvShowsPagingSource
import com.anshtya.data.paging.tv.OnAirTvShowsPagingSource
import com.anshtya.data.paging.tv.PopularTvShowsPagingSource
import com.anshtya.data.paging.tv.TopRatedTvShowsPagingSource
import com.anshtya.data.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 20

class TvShowRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : TvShowRepository {
    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        initialLoadSize = PAGE_SIZE,
        prefetchDistance = 1
    )
    override fun getAiringTodayTvShows(): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { AiringTodayTvShowsPagingSource(tmdbApi) }
        ).flow
    }

    override fun getPopularTvShows(): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PopularTvShowsPagingSource(tmdbApi) }
        ).flow
    }

    override fun getTopRatedTvShows(): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { TopRatedTvShowsPagingSource(tmdbApi) }
        ).flow
    }

    override fun getOnAirTvShows(): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { OnAirTvShowsPagingSource(tmdbApi) }
        ).flow
    }
}