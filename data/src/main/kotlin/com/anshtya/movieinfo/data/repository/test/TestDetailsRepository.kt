package com.anshtya.movieinfo.data.repository.test

import com.anshtya.movieinfo.data.model.NetworkResponse
import com.anshtya.movieinfo.data.model.details.MovieDetails
import com.anshtya.movieinfo.data.model.details.people.PersonDetails
import com.anshtya.movieinfo.data.model.details.tv.TvDetails
import com.anshtya.movieinfo.data.repository.DetailsRepository
import com.anshtya.movieinfo.data.repository.test.data.testMovieDetail
import com.anshtya.movieinfo.data.repository.test.data.testPersonDetails
import com.anshtya.movieinfo.data.repository.test.data.testTvShowDetails

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