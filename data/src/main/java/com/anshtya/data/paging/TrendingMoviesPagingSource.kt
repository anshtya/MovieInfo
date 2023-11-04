package com.anshtya.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anshtya.data.model.StreamingItem
import com.anshtya.data.model.asModel
import com.anshtya.network.retrofit.TmdbApi

class TrendingMoviesPagingSource(
    private val tmdbApi: TmdbApi,
    private val timeWindow: String
) : PagingSource<Int, StreamingItem>() {
    override fun getRefreshKey(state: PagingState<Int, StreamingItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StreamingItem> {
        val pageNumber = params.key ?: 1
        return try {
            val response = tmdbApi.getTrendingMovies(
                timeWindow = timeWindow,
                page = pageNumber
            )
            LoadResult.Page(
                data = response.results.map { it.asModel() },
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (response.totalPages == pageNumber) null else pageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}