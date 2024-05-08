package com.anshtya.movieinfo.core.model.details.tv

import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.content.ContentItem
import com.anshtya.movieinfo.core.model.details.people.Credits
import com.anshtya.movieinfo.core.model.library.LibraryItem

data class TvDetails(
    val adult: Boolean,
    val backdropPath: String,
    val createdBy: List<CreatedBy>,
    val credits: Credits,
    val episodeRunTime: String,
    val firstAirDate: String,
    val genres: String,
    val id: Int,
    val inProduction: String,
    val lastAirDate: String,
    val lastEpisodeToAir: EpisodeDetails,
    val name: String,
    val networks: String,
    val nextEpisodeToAir: EpisodeDetails?,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val originCountry: List<String>,
    val originalLanguage: String,
    val overview: String,
    val posterPath: String,
    val productionCompanies: String,
    val productionCountries: String,
    val rating: Double,
    val recommendations: List<ContentItem>,
    val releaseYear: Int,
//    val seasons: List<Season>,
    val status: String,
    val tagline: String,
    val type: String,
    val voteCount: Int
) {
    fun asLibraryItem() = LibraryItem(
        id = id,
        imagePath = posterPath,
        name = name,
        mediaType = MediaType.TV.name.lowercase(),
    )
}