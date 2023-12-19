package com.anshtya.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anshtya.data.model.SearchItem
import com.anshtya.data.model.SearchSuggestion
import com.anshtya.data.model.asModel
import com.anshtya.data.paging.SearchMoviePagingSource
import com.anshtya.data.paging.SearchTVPagingSource
import com.anshtya.network.retrofit.TmdbApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : SearchRepository {
    private val pagingConfig = PagingConfig(
        pageSize = 20,
        prefetchDistance = 2
    )

    override suspend fun multiSearch(query: String): List<SearchSuggestion> {
        return tmdbApi.multiSearch(query = query).results
            .map { suggestion -> suggestion.asModel() }
    }

    override fun searchMovie(query: String): Flow<PagingData<SearchItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { SearchMoviePagingSource(tmdbApi, query) }
        ).flow
    }

    override fun searchTV(query: String): Flow<PagingData<SearchItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { SearchTVPagingSource(tmdbApi, query) }
        ).flow
    }
}