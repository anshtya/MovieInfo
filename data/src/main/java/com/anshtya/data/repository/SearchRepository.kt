package com.anshtya.data.repository

import com.anshtya.core.model.SearchItem
import com.anshtya.data.model.NetworkResponse

interface SearchRepository {
    suspend fun getSearchSuggestions(
        query: String,
        includeAdult: Boolean
    ): NetworkResponse<List<SearchItem>>
}