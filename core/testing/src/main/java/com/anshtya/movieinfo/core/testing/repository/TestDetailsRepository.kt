package com.anshtya.movieinfo.core.testing.repository

import com.anshtya.movieinfo.core.model.NetworkResponse
import com.anshtya.movieinfo.core.model.details.MovieDetails
import com.anshtya.movieinfo.core.model.details.people.PersonDetails
import com.anshtya.movieinfo.core.model.details.tv.TvDetails
import com.anshtya.movieinfo.core.testing.util.testMovieDetail
import com.anshtya.movieinfo.core.testing.util.testPersonDetails
import com.anshtya.movieinfo.core.testing.util.testTvShowDetails
import com.anshtya.movieinfo.data.repository.DetailsRepository

class TestDetailsRepository : DetailsRepository {
    private var generateError = false

    override suspend fun getMovieDetails(id: Int): NetworkResponse<MovieDetails> {
        return if (!generateError) {
            NetworkResponse.Success(data = testMovieDetail)
        } else {
            NetworkResponse.Error()
        }
    }

    override suspend fun getTvShowDetails(id: Int): NetworkResponse<TvDetails> {
        return if (!generateError) {
            NetworkResponse.Success(data = testTvShowDetails)
        } else {
            NetworkResponse.Error()
        }
    }

    override suspend fun getPersonDetails(id: Int): NetworkResponse<PersonDetails> {
        return if (!generateError) {
            NetworkResponse.Success(data = testPersonDetails)
        } else {
            NetworkResponse.Error()
        }
    }

    fun generateError(value: Boolean) {
        generateError = value
    }
}