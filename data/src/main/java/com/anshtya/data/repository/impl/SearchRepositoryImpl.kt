package com.anshtya.data.repository.impl

import com.anshtya.core.model.SearchItem
import com.anshtya.core.network.model.search.asModel
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.core.model.NetworkResponse
import com.anshtya.data.repository.SearchRepository
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