package com.anshtya.movieinfo.core.data.repository

import com.anshtya.movieinfo.core.model.SearchItem
import com.anshtya.movieinfo.core.model.NetworkResponse

interface SearchRepository {
    suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): NetworkResponse<List<SearchItem>>
}