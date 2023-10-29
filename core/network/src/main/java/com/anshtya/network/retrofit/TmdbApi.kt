package com.anshtya.network.retrofit

import com.anshtya.network.model.NetworkTrendingMovies
import retrofit2.http.GET
import retrofit2.http.Path

interface TmdbApi {
    @GET("trending/movie/{time_window}?language=en-US")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String
    ): NetworkTrendingMovies
}