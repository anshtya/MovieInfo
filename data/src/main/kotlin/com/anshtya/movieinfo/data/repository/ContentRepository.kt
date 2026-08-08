package com.anshtya.movieinfo.data.repository

import com.anshtya.movieinfo.data.model.content.ContentItem
import com.anshtya.movieinfo.data.model.content.MovieListCategory
import com.anshtya.movieinfo.data.model.content.TvShowListCategory

interface ContentRepository {
    suspend fun getMovieItems(
        page: Int,
        category: MovieListCategory
    ): Result<List<ContentItem>>

    suspend fun getTvShowItems(
        page: Int,
        category: TvShowListCategory
    ): Result<List<ContentItem>>
}