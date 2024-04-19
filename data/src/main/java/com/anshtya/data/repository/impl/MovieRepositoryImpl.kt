package com.anshtya.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anshtya.core.model.content.ContentItem
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.paging.movies.NowPlayingMoviesPagingSource
import com.anshtya.data.paging.movies.PopularMoviesPagingSource
import com.anshtya.data.paging.movies.TopRatedMoviesPagingSource
import com.anshtya.data.paging.movies.UpcomingMoviesPagingSource
import com.anshtya.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 20

internal class MovieRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : MovieRepository {
    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        initialLoadSize = PAGE_SIZE,
        prefetchDistance = 1
    )

    override fun getNowPlayingMovies(): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { NowPlayingMoviesPagingSource(tmdbApi) }
        ).flow
    }

    override fun getPopularMovies(): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PopularMoviesPagingSource(tmdbApi) }
        ).flow
    }

    override fun getTopRatedMovies(): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { TopRatedMoviesPagingSource(tmdbApi) }
        ).flow
    }

    override fun getUpcomingMovies(): Flow<PagingData<ContentItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { UpcomingMoviesPagingSource(tmdbApi) }
        ).flow
    }
}