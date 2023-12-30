package com.anshtya.core.network.retrofit

import com.anshtya.core.network.model.MultiSearchResponse
import com.anshtya.core.network.model.SearchResponse
import com.anshtya.core.network.model.StreamingItemResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("discover/{content_type}?include_video=false&with_watch_monetization_types=free&sort_by=popularity.desc")
    suspend fun getFreeContent(
        @Path("content_type") contentType: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String = "US",
        @Query("language") language: String ="en-US"
    ): StreamingItemResponse

    @GET("discover/movie?include_video=false&with_watch_monetization_types=flatrate&sort_by=popularity.desc")
    suspend fun getPopularStreamingTitles(
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String = "US",
        @Query("language") language: String ="en-US"
    ): StreamingItemResponse

    @GET("discover/movie?include_video=false&with_watch_monetization_types=rent&sort_by=popularity.desc")
    suspend fun getPopularTitlesOnRent(
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String = "US",
        @Query("language") language: String ="en-US"
    ): StreamingItemResponse

    @GET("discover/movie?include_video=false&with_release_type=3|2&sort_by=popularity.desc")
    suspend fun getPopularTitlesInTheatres(
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean,
        @Query("watch_region") watchRegion: String = "US",
        @Query("language") language: String ="en-US"
    ): StreamingItemResponse

    @GET("trending/movie/{time_window}?language=en-US")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String,
        @Query("page") page: Int
    ): StreamingItemResponse

    @GET("search/multi")
    suspend fun multiSearch(
        @Query("page") page: Int = 1,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean
    ): MultiSearchResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("page") page: Int = 1,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean
    ): SearchResponse

    @GET("search/tv")
    suspend fun searchTV(
        @Query("page") page: Int = 1,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean
    ): SearchResponse
}