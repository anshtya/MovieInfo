package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.data.model.FreeItem
import com.anshtya.data.model.PopularContentType
import com.anshtya.data.model.PopularItem
import com.anshtya.data.model.TrendingItem
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun getFreeContent(contentType: String, includeAdult: Boolean): Flow<PagingData<FreeItem>>
    fun getTrendingMovies(timeWindow: String): Flow<PagingData<TrendingItem>>
    fun getPopularContent(
        contentType: PopularContentType,
        includeAdult: Boolean
    ): Flow<PagingData<PopularItem>>
}