package com.anshtya.movieinfo.core.data.testdoubles.repository

import com.anshtya.movieinfo.core.data.repository.SearchRepository
import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.model.SearchItem
import com.anshtya.movieinfo.core.data.testdoubles.testSearchResults

class TestSearchRepository: SearchRepository {
    private var generateError = false

    override suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): NetworkResponse<List<SearchItem>> {
        if (generateError) return NetworkResponse.Error()

        return NetworkResponse.Success(testSearchResults)
    }

    fun generateError(value: Boolean) {
        generateError = value
    }
}