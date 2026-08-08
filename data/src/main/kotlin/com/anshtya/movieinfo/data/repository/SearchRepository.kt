package com.anshtya.movieinfo.data.repository

import com.anshtya.movieinfo.data.model.SearchItem

interface SearchRepository {
    suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): Result<List<SearchItem>>
}