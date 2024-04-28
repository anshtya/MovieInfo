package com.anshtya.data.repository

import com.anshtya.core.model.details.MovieDetails
import com.anshtya.core.model.details.people.PersonDetails
import com.anshtya.core.model.details.tv.TvDetails
import com.anshtya.core.model.NetworkResponse

interface DetailsRepository {
    suspend fun getMovieDetails(id: Int): NetworkResponse<MovieDetails>
    suspend fun getTvShowDetails(id: Int): NetworkResponse<TvDetails>
    suspend fun getPersonDetails(id: Int): NetworkResponse<PersonDetails>
}