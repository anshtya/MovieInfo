package com.anshtya.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anshtya.core.model.content.ContentItem
import com.anshtya.core.network.model.content.NetworkContentItem
import com.anshtya.core.network.retrofit.TmdbApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class TvShowListPagingSource @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val categoryName: String
) : PagingSource<Int, ContentItem>() {
    override fun getRefreshKey(state: PagingState<Int, ContentItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentItem> {
        val pageNumber = params.key ?: 1
        return try {
            val response = tmdbApi.getTvShowLists(
                category = categoryName,
                page = pageNumber
            )
            LoadResult.Page(
                data = response.results.map(NetworkContentItem::asModel),
                prevKey = null,
                nextKey = if (response.totalPages == pageNumber) null else response.page + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}