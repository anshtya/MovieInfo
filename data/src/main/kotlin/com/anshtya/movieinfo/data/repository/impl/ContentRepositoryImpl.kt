package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.core.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.core.network.datasource.TmdbNetworkDataSource
import com.anshtya.movieinfo.core.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.data.model.content.ContentItem
import com.anshtya.movieinfo.data.model.content.MovieListCategory
import com.anshtya.movieinfo.data.model.content.TvShowListCategory
import com.anshtya.movieinfo.data.model.content.asModel
import com.anshtya.movieinfo.data.repository.ContentRepository
import com.anshtya.movieinfo.data.repository.util.toResult
import javax.inject.Inject

internal class ContentRepositoryImpl @Inject constructor(
    private val networkDataSource: TmdbNetworkDataSource,
    private val accountDetailsDao: AccountDetailsDao
) : ContentRepository {
    override suspend fun getMovieItems(
        page: Int,
        category: MovieListCategory
    ): Result<List<ContentItem>> {
        return networkDataSource.getMovieLists(
            category = category.categoryName,
            page = page,
            region = accountDetailsDao.getRegionCode()
        ).toResult { response ->
            response.results.map(NetworkContentItem::asModel)
        }
    }

    override suspend fun getTvShowItems(
        page: Int,
        category: TvShowListCategory
    ): Result<List<ContentItem>> {
        return networkDataSource.getTvShowLists(
            category = category.categoryName,
            page = page
        ).toResult { response ->
            response.results.map(NetworkContentItem::asModel)
        }
    }
}