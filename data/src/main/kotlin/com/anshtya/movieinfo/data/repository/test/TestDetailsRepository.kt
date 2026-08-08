package com.anshtya.movieinfo.data.repository.test

import com.anshtya.movieinfo.data.model.details.MovieDetails
import com.anshtya.movieinfo.data.model.details.people.PersonDetails
import com.anshtya.movieinfo.data.model.details.tv.TvDetails
import com.anshtya.movieinfo.data.repository.DetailsRepository
import com.anshtya.movieinfo.data.repository.test.data.testMovieDetail
import com.anshtya.movieinfo.data.repository.test.data.testPersonDetails
import com.anshtya.movieinfo.data.repository.test.data.testTvShowDetails

class TestDetailsRepository : DetailsRepository {
    private var generateError = false

    override suspend fun getMovieDetails(id: Int): Result<MovieDetails> {
        return if (!generateError) {
            Result.success(testMovieDetail)
        } else {
            Result.failure(Exception("An error occurred"))
        }
    }

    override suspend fun getTvShowDetails(id: Int): Result<TvDetails> {
        return if (!generateError) {
            Result.success(testTvShowDetails)
        } else {
            Result.failure(Exception("An error occurred"))
        }
    }

    override suspend fun getPersonDetails(id: Int): Result<PersonDetails> {
        return if (!generateError) {
            Result.success(testPersonDetails)
        } else {
            Result.failure(Exception("An error occurred"))
        }
    }

    fun generateError(value: Boolean) {
        generateError = value
    }
}