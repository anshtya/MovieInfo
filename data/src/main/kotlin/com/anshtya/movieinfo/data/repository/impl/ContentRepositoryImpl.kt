package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.data.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.data.model.NetworkResponse
import com.anshtya.movieinfo.data.model.content.ContentItem
import com.anshtya.movieinfo.data.model.content.MovieListCategory
import com.anshtya.movieinfo.data.model.content.TvShowListCategory
import com.anshtya.movieinfo.data.network.model.content.NetworkContentItem
import com.anshtya.movieinfo.data.network.retrofit.TmdbApi
import com.anshtya.movieinfo.data.repository.ContentRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class ContentRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val accountDetailsDao: AccountDetailsDao
) : ContentRepository {
    override suspend fun getMovieItems(
        page: Int,
        category: MovieListCategory
    ): NetworkResponse<List<ContentItem>> {
        return try {
            val response = tmdbApi.getMovieLists(
                category = category.categoryName,
                page = page,
                region = accountDetailsDao.getRegionCode()
            )
            NetworkResponse.Success(response.results.map(
                NetworkContentItem::asModel))
        } catch (e: IOException) {
            return NetworkResponse.Error()
        } catch (e: HttpException) {
            return NetworkResponse.Error(e.message)
        }
    }

    override suspend fun getTvShowItems(
        page: Int,
        category: TvShowListCategory
    ): NetworkResponse<List<ContentItem>> {
        return try {
            val response = tmdbApi.getTvShowLists(
                category = category.categoryName,
                page = page
            )
            NetworkResponse.Success(response.results.map(NetworkContentItem::asModel))
        } catch (e: IOException) {
            return NetworkResponse.Error()
        } catch (e: HttpException) {
            return NetworkResponse.Error(e.message)
        }
    }
}