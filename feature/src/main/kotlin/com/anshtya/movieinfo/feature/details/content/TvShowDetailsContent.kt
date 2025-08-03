package com.anshtya.movieinfo.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.details.tv.TvDetails
import com.anshtya.movieinfo.data.model.library.LibraryItem
import com.anshtya.movieinfo.feature.R

@Composable
internal fun TvShowDetailsContent(
    tvDetails: TvDetails,
    isFavorite: Boolean,
    isAddedToWatchList: Boolean,
    onFavoriteClick: (LibraryItem) -> Unit,
    onWatchlistClick: (LibraryItem) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onCastClick: (String) -> Unit,
    onRecommendationClick: (String) -> Unit,
    onBackdropCollapse: (Boolean) -> Unit,
) {
    MediaDetailsContent(
        backdropPath = tvDetails.backdropPath,
        voteCount = tvDetails.voteCount,
        name = tvDetails.name,
        rating = tvDetails.rating,
        releaseYear = tvDetails.releaseYear,
        runtime = tvDetails.episodeRunTime,
        tagline = tvDetails.tagline,
        genres = tvDetails.genres,
        overview = tvDetails.overview,
        cast = tvDetails.credits.cast.take(10),
        recommendations = tvDetails.recommendations,
        isFavorite = isFavorite,
        isAddedToWatchList = isAddedToWatchList,
        onFavoriteClick = { onFavoriteClick(tvDetails.asLibraryItem()) },
        onWatchlistClick = { onWatchlistClick(tvDetails.asLibraryItem()) },
        onSeeAllCastClick = onSeeAllCastClick,
        onCastClick = onCastClick,
        onRecommendationClick = { id ->
            onRecommendationClick("${id},${MediaType.TV}")
        },
        onBackdropCollapse = onBackdropCollapse
    ) {
        TvDetailsSection(
            originalLanguage = tvDetails.originalLanguage,
            firstAirDate = tvDetails.firstAirDate,
            lastAirDate = tvDetails.lastAirDate,
            inProduction = tvDetails.inProduction,
            status = tvDetails.status,
            nextAirDate = tvDetails.nextEpisodeToAir?.airDate,
            numberOfEpisodes = tvDetails.numberOfEpisodes,
            numberOfSeasons = tvDetails.numberOfSeasons,
            networks = tvDetails.networks,
            productionCompanies = tvDetails.productionCompanies,
            productionCountries = tvDetails.productionCountries
        )
    }
}

@Composable
private fun TvDetailsSection(
    originalLanguage: String,
    firstAirDate: String,
    lastAirDate: String,
    inProduction: String,
    status: String,
    nextAirDate: String?,
    numberOfEpisodes: Int,
    numberOfSeasons: Int,
    networks: String,
    productionCompanies: String,
    productionCountries: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        DetailItem(
            fieldName = stringResource(id = R.string.original_language),
            value = originalLanguage
        )

        DetailItem(
            fieldName = stringResource(id = R.string.first_air_date),
            value = firstAirDate
        )

        DetailItem(
            fieldName = stringResource(id = R.string.last_air_date),
            value = lastAirDate
        )

        DetailItem(
            fieldName = stringResource(id = R.string.in_production),
            value = inProduction
        )

        DetailItem(
            fieldName = stringResource(id = R.string.status),
            value = status
        )

        nextAirDate?.let {
            DetailItem(
                fieldName = stringResource(id = R.string.next_air_date),
                value = it
            )
        }

        DetailItem(
            fieldName = stringResource(id = R.string.number_episodes),
            value = "$numberOfEpisodes"
        )

        DetailItem(
            fieldName = stringResource(id = R.string.number_seasons),
            value = "$numberOfSeasons"
        )

        DetailItem(
            fieldName = stringResource(id = R.string.networks),
            value = networks
        )

        DetailItem(
            fieldName = stringResource(id = R.string.production_companies),
            value = productionCompanies
        )

        DetailItem(
            fieldName = stringResource(id = R.string.production_countries),
            value = productionCountries
        )
    }
}