package com.anshtya.data.repository

import com.anshtya.core.model.MovieDetails
import com.anshtya.core.model.PersonDetails
import com.anshtya.core.model.TvDetails
import com.anshtya.core.network.model.asModel
import com.anshtya.core.network.retrofit.TmdbApi
import com.anshtya.data.model.NetworkResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : DetailsRepository {
    override suspend fun getMovieDetails(id: Int): NetworkResponse<MovieDetails> {
        return try {
            val response = tmdbApi.getMovieDetails(id).asModel()
            NetworkResponse.Success(response)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            NetworkResponse.Error()
        }
    }

    override suspend fun getTvShowDetails(id: Int): NetworkResponse<TvDetails> {
        return try {
            val response = tmdbApi.getTvShowDetails(id).asModel()
            NetworkResponse.Success(response)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            NetworkResponse.Error()
        }
    }

    override suspend fun getPersonDetails(id: Int): NetworkResponse<PersonDetails> {
        return try {
            val response = tmdbApi.getPersonDetails(id).asModel()
            NetworkResponse.Success(response)
        } catch (e: IOException) {
            NetworkResponse.Error()
        } catch (e: HttpException) {
            NetworkResponse.Error()
        }
    }
}