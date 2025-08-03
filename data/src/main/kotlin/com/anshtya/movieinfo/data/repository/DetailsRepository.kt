package com.anshtya.movieinfo.data.repository

import com.anshtya.movieinfo.data.model.NetworkResponse
import com.anshtya.movieinfo.data.model.details.MovieDetails
import com.anshtya.movieinfo.data.model.details.people.PersonDetails
import com.anshtya.movieinfo.data.model.details.tv.TvDetails

interface DetailsRepository {
    suspend fun getMovieDetails(id: Int): NetworkResponse<MovieDetails>
    suspend fun getTvShowDetails(id: Int): NetworkResponse<TvDetails>
    suspend fun getPersonDetails(id: Int): NetworkResponse<PersonDetails>
}