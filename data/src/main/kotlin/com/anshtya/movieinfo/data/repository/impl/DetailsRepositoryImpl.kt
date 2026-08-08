package com.anshtya.movieinfo.data.repository.impl

import com.anshtya.movieinfo.core.network.datasource.TmdbNetworkDataSource
import com.anshtya.movieinfo.data.model.details.MovieDetails
import com.anshtya.movieinfo.data.model.details.asModel
import com.anshtya.movieinfo.data.model.details.people.PersonDetails
import com.anshtya.movieinfo.data.model.details.people.asModel
import com.anshtya.movieinfo.data.model.details.tv.TvDetails
import com.anshtya.movieinfo.data.model.details.tv.asModel
import com.anshtya.movieinfo.data.repository.DetailsRepository
import com.anshtya.movieinfo.data.repository.util.toResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepositoryImpl @Inject constructor(
    private val networkDataSource: TmdbNetworkDataSource
) : DetailsRepository {
    override suspend fun getMovieDetails(id: Int): Result<MovieDetails> {
        return networkDataSource.getMovieDetails(id).toResult { it.asModel() }
    }

    override suspend fun getTvShowDetails(id: Int): Result<TvDetails> {
        return networkDataSource.getTvShowDetails(id).toResult { it.asModel() }
    }

    override suspend fun getPersonDetails(id: Int): Result<PersonDetails> {
        return networkDataSource.getPersonDetails(id).toResult { it.asModel() }
    }
}