package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.core.model.content.ContentItem
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {
    fun getAiringTodayTvShows(): Flow<PagingData<ContentItem>>

    fun getPopularTvShows(): Flow<PagingData<ContentItem>>

    fun getTopRatedTvShows(): Flow<PagingData<ContentItem>>

    fun getOnAirTvShows(): Flow<PagingData<ContentItem>>

}