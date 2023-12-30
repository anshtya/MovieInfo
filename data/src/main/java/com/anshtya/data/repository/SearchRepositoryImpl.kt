package com.anshtya.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anshtya.core.model.SearchItem
import com.anshtya.core.model.SearchSuggestion
import com.anshtya.core.network.model.asModel
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.model.Response
import com.anshtya.data.paging.SearchMoviePagingSource
import com.anshtya.data.paging.SearchTVPagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : SearchRepository {
    private val pagingConfig = PagingConfig(
        pageSize = 20,
        prefetchDistance = 2
    )

    override suspend fun multiSearch(
        query: String,
        includeAdult: Boolean
    ): Response<List<SearchSuggestion>> {
        return try {
            val result = tmdbApi.multiSearch(
                query = query,
                includeAdult = includeAdult
            )
                .results.take(6)
                .map { suggestion -> suggestion.asModel() }
            Response.Success(result)
        } catch (e: IOException) {
            Response.Error
        } catch (e: HttpException) {
            Response.Error
        }
    }

    override fun searchMovie(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { SearchMoviePagingSource(
                tmdbApi = tmdbApi,
                query = query,
                includeAdult = includeAdult
            ) }
        ).flow
    }

    override fun searchTV(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { SearchTVPagingSource(
                tmdbApi = tmdbApi,
                query = query,
                includeAdult = includeAdult
            ) }
        ).flow
    }
}