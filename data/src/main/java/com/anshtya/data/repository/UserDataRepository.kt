package com.anshtya.data.repository

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val trendingContentFilterIndex: Flow<Int>
    val freeContentFilterIndex: Flow<Int>
    val popularContentFilterIndex: Flow<Int>

    suspend fun setTrendingContentFilterIndex(index: Int)
    suspend fun setFreeContentFilterIndex(index: Int)
    suspend fun setPopularContentFilterIndex(index: Int)
}