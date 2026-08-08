package com.anshtya.movieinfo.data.repository.test

import com.anshtya.movieinfo.data.model.SearchItem
import com.anshtya.movieinfo.data.repository.SearchRepository
import com.anshtya.movieinfo.data.repository.test.data.testSearchResults

class TestSearchRepository: SearchRepository {
    private var generateError = false

    override suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): Result<List<SearchItem>> {
        if (generateError) return Result.failure(Exception("An error occurred"))

        return Result.success(testSearchResults)
    }

    fun generateError(value: Boolean) {
        generateError = value
    }
}