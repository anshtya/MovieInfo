package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.core.model.SearchItem
import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.network.model.search.asModel
import com.anshtya.movieinfo.core.network.retrofit.TmdbApi
import com.anshtya.movieinfo.data.repository.SearchRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : SearchRepository {

    override suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): NetworkResponse<List<SearchItem>> {
        return try {
            val result = tmdbApi.multiSearch(
                query = query,
                includeAdult = includeAdult
            ).results
                .map { suggestion -> suggestion.asModel() }
            NetworkResponse.Success(result)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            NetworkResponse.Error()
        }
    }
}