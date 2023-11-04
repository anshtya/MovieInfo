package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.data.model.StreamingItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getTrendingMovies(timeWindow: String): Flow<PagingData<StreamingItem>>
}