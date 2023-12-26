package com.anshtya.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anshtya.data.model.SearchItem
import com.anshtya.data.model.asModel
import com.anshtya.network.retrofit.TmdbApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class SearchMoviePagingSource @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val query: String
): PagingSource<Int, SearchItem>() {
    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        val pageNumber = params.key ?: 1
        return try {
            val response = tmdbApi.searchMovie(page = pageNumber, query = query)
            LoadResult.Page(
                data = response.results.map { it.asModel() },
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (response.totalPages == pageNumber) null else pageNumber + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}