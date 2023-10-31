package com.anshtya.data.repository

import com.anshtya.network.model.StreamingItemResponse

interface HomeRepository {
    suspend fun getTrendingMovies(timeWindow: String): StreamingItemResponse
}