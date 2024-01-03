package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.core.model.SearchItem
import com.anshtya.core.model.SearchSuggestion
import com.anshtya.data.model.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun multiSearch(query: String, includeAdult: Boolean): NetworkResponse<List<SearchSuggestion>>
    fun searchMovie(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>>
    fun searchTV(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>>
}