package com.anshtya.data.repository

import com.anshtya.network.model.StreamingItemResponse
import com.anshtya.network.retrofit.TmdbApi
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): HomeRepository {
    override suspend fun getTrendingMovies(timeWindow: String): StreamingItemResponse {
        return tmdbApi.getTrendingMovies("")
    }
}