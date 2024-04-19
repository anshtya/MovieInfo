package com.anshtya.data.repository

import androidx.paging.PagingData
import com.anshtya.core.model.content.ContentItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getNowPlayingMovies(): Flow<PagingData<ContentItem>>

    fun getPopularMovies(): Flow<PagingData<ContentItem>>

    fun getTopRatedMovies(): Flow<PagingData<ContentItem>>

    fun getUpcomingMovies(): Flow<PagingData<ContentItem>>

}