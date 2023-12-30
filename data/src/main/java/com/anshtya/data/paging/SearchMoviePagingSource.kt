package com.anshtya.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anshtya.core.model.SearchItem
import com.anshtya.core.network.model.asModel
import com.anshtya.core.network.retrofit.TmdbApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class SearchMoviePagingSource @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val query: String,
    private val includeAdult: Boolean
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
            val response = tmdbApi.searchMovie(
                page = pageNumber,
                query = query,
                includeAdult = includeAdult
            )
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