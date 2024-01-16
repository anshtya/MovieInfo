package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.core.model.FreeContentType
import com.anshtya.core.model.PopularContentType
import com.anshtya.core.model.TrendingContentTimeWindow
import com.anshtya.core.model.ContentItem
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun getFreeContent(
        contentType: FreeContentType,
        includeAdult: Boolean,
        shouldReload: Boolean
    ): Flow<PagingData<ContentItem>>

    fun getTrendingMovies(
        timeWindow: TrendingContentTimeWindow,
        shouldReload: Boolean
    ): Flow<PagingData<ContentItem>>

    fun getPopularContent(
        contentType: PopularContentType,
        includeAdult: Boolean,
        shouldReload: Boolean
    ): Flow<PagingData<ContentItem>>
}