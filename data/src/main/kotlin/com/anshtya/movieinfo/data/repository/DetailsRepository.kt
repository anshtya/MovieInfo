package com.anshtya.movieinfo.data.repository

import com.anshtya.movieinfo.data.model.details.MovieDetails
import com.anshtya.movieinfo.data.model.details.people.PersonDetails
import com.anshtya.movieinfo.data.model.details.tv.TvDetails

interface DetailsRepository {
    suspend fun getMovieDetails(id: Int): Result<MovieDetails>
    suspend fun getTvShowDetails(id: Int): Result<TvDetails>
    suspend fun getPersonDetails(id: Int): Result<PersonDetails>
}