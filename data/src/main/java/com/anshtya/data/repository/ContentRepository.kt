package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.core.model.content.FreeContentType
import com.anshtya.core.model.content.PopularContentType
import com.anshtya.core.model.content.TrendingContentTimeWindow
import com.anshtya.core.model.content.ContentItem
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