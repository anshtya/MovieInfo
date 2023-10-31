package com.anshtya.network.retrofit

import com.anshtya.network.model.StreamingItemResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("discover/movie?include_video=false&with_watch_monetization_types=flatrate&sort_by=popularity.desc")
    suspend fun getPopularStreamingTitles(
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String,
        @Query("language") language: String
    ): StreamingItemResponse

    @GET("discover/movie?include_video=false&with_watch_monetization_types=rent&sort_by=popularity.desc")
    suspend fun getPopularTitlesOnRent(
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String,
        @Query("language") language: String
    ): StreamingItemResponse

    @GET("discover/movie?include_video=false&with_release_type=3|2&sort_by=popularity.desc")
    suspend fun getPopularTitlesInTheatres(
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String,
        @Query("language") language: String
    ): StreamingItemResponse

    @GET("trending/movie/{time_window}?language=en-US")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String
    ): StreamingItemResponse
}