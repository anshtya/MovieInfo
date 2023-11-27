package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.data.model.StreamingItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getFreeContent(contentType: String): Flow<PagingData<StreamingItem>>
    fun getPopularStreamingTitles(): Flow<PagingData<StreamingItem>>
    fun getPopularTitlesInTheatres(): Flow<PagingData<StreamingItem>>
    fun getPopularTitlesOnRent(): Flow<PagingData<StreamingItem>>
    fun getTrendingMovies(timeWindow: String): Flow<PagingData<StreamingItem>>
}