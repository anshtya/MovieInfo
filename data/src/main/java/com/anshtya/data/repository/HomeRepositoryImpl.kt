package com.anshtya.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anshtya.data.model.StreamingItem
import com.anshtya.data.paging.FreeContentPagingSource
import com.anshtya.data.paging.PopularStreamingTitlesPagingSource
import com.anshtya.data.paging.PopularTitlesInTheatresPagingSource
import com.anshtya.data.paging.PopularTitlesOnRentPagingSource
import com.anshtya.data.paging.TrendingMoviesPagingSource
import com.anshtya.network.retrofit.TmdbApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : HomeRepository {
    private val pagingConfig = PagingConfig(pageSize = 20)

    override fun getFreeContent(contentType: String): Flow<PagingData<StreamingItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { FreeContentPagingSource(tmdbApi, contentType) }
        ).flow
    }

    override fun getPopularStreamingTitles(): Flow<PagingData<StreamingItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PopularStreamingTitlesPagingSource(tmdbApi) }
        ).flow
    }

    override fun getPopularTitlesInTheatres(): Flow<PagingData<StreamingItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PopularTitlesInTheatresPagingSource(tmdbApi) }
        ).flow
    }

    override fun getPopularTitlesOnRent(): Flow<PagingData<StreamingItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PopularTitlesOnRentPagingSource(tmdbApi) }
        ).flow
    }

    override fun getTrendingMovies(timeWindow: String): Flow<PagingData<StreamingItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { TrendingMoviesPagingSource(tmdbApi, timeWindow) }
        ).flow
    }
}