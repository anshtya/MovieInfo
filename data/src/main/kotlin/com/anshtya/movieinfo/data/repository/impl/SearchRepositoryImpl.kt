package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.core.network.datasource.TmdbNetworkDataSource
import com.anshtya.movieinfo.data.model.SearchItem
import com.anshtya.movieinfo.data.model.asModel
import com.anshtya.movieinfo.data.repository.SearchRepository
import com.anshtya.movieinfo.data.repository.util.toResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val networkDataSource: TmdbNetworkDataSource
) : SearchRepository {

    override suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): Result<List<SearchItem>> {
        return networkDataSource.multiSearch(
            query = query,
            includeAdult = includeAdult
        ).toResult { response ->
            response.results.map { suggestion -> suggestion.asModel() }
        }
    }
}