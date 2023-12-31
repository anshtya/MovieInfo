package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.core.model.FreeContentType
import com.anshtya.core.model.FreeItem
import com.anshtya.core.model.PopularContentType
import com.anshtya.core.model.PopularItem
import com.anshtya.core.model.TrendingContentTimeWindow
import com.anshtya.core.model.TrendingItem
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun getFreeContent(
        contentType: FreeContentType,
        includeAdult: Boolean,
        shouldReload: Boolean
    ): Flow<PagingData<FreeItem>>

    fun getTrendingMovies(
        timeWindow: TrendingContentTimeWindow,
        shouldReload: Boolean
    ): Flow<PagingData<TrendingItem>>

    fun getPopularContent(
        contentType: PopularContentType,
        includeAdult: Boolean,
        shouldReload: Boolean
    ): Flow<PagingData<PopularItem>>
}