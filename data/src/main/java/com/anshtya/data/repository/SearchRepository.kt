package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.data.model.Response
import com.anshtya.data.model.SearchItem
import com.anshtya.data.model.SearchSuggestion
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun multiSearch(query: String, includeAdult: Boolean): Response<List<SearchSuggestion>>
    fun searchMovie(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>>
    fun searchTV(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>>
}