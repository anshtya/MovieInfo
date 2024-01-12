package com.anshtya.data.repository

import com.anshtya.core.model.MovieDetails
import com.anshtya.core.model.PersonDetails
import com.anshtya.core.model.TvDetails
import com.anshtya.data.model.NetworkResponse

interface DetailsRepository {
    suspend fun getMovieDetails(id: Int): NetworkResponse<MovieDetails>
    suspend fun getTvShowDetails(id: Int): NetworkResponse<TvDetails>
    suspend fun getPersonDetails(id: Int): NetworkResponse<PersonDetails>
}