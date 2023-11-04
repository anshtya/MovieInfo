package com.anshtya.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anshtya.data.model.StreamingItem
import com.anshtya.data.paging.TrendingMoviesPagingSource
import com.anshtya.network.retrofit.TmdbApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): HomeRepository {
    override fun getTrendingMovies(timeWindow: String): Flow<PagingData<StreamingItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                TrendingMoviesPagingSource(tmdbApi, timeWindow)
            }
        ).flow
    }
}

const val PAGE_SIZE = 20