package com.anshtya.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anshtya.data.model.StreamingItem
import com.anshtya.data.model.asModel
import com.anshtya.network.retrofit.TmdbApi
import javax.inject.Inject

class PopularTitlesOnRentPagingSource @Inject constructor(
    private val tmdbApi: TmdbApi
): PagingSource<Int, StreamingItem>() {
    override fun getRefreshKey(state: PagingState<Int, StreamingItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StreamingItem> {
        val pageNumber = params.key ?: 1
        return try {
            val response = tmdbApi.getPopularTitlesOnRent(page = pageNumber)
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