package com.anshtya.movieinfo.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.ui.ContentSectionHeader
import com.anshtya.movieinfo.core.ui.LazyRowContentSection
import com.anshtya.movieinfo.core.ui.MediaItemCard
import com.anshtya.movieinfo.core.ui.noRippleClickable
import com.anshtya.movieinfo.feature.details.BackdropImageSection
import com.anshtya.movieinfo.feature.details.CastItem
import com.anshtya.movieinfo.feature.details.DetailItem
import com.anshtya.movieinfo.feature.details.InfoSection
import com.anshtya.movieinfo.feature.details.LibraryActions
import com.anshtya.movieinfo.feature.details.OverviewSection
import com.anshtya.movieinfo.core.model.details.tv.TvDetails
import com.anshtya.movieinfo.core.model.library.LibraryItem
import com.anshtya.movieinfo.feature.details.R
import com.anshtya.movieinfo.feature.details.backdropHeight

@Composable
internal fun TvShowDetailsContent(
    tvDetails: TvDetails,
    isFavorite: Boolean,
    isAddedToWatchList: Boolean,
    onFavoriteClick: (LibraryItem) -> Unit,
    onWatchlistClick: (LibraryItem) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BackdropImageSection(
            path = tvDetails.backdropPath,
            modifier = Modifier
                .fillMaxWidth()
                .height(backdropHeight)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                MediaItemCard(
                    posterPath = tvDetails.posterPath,
                    modifier = Modifier.size(height = 200.dp, width = 140.dp)
                )

                Spacer(Modifier.width(10.dp))

                InfoSection(
                    count = tvDetails.voteCount,
                    genres = tvDetails.genres,
                    name = tvDetails.name,
                    rating = tvDetails.rating,
                    releaseYear = tvDetails.releaseYear,
                    tagline = tvDetails.tagline,
                    runtime = tvDetails.episodeRunTime
                )
            }

            LibraryActions(
                isFavorite = isFavorite,
                isAddedToWatchList = isAddedToWatchList,
                onFavoriteClick = { onFavoriteClick(tvDetails.asLibraryItem()) },
                onWatchlistClick = { onWatchlistClick(tvDetails.asLibraryItem() )}
            )

            LazyRowContentSection(
                pagingEnabled = false,
                sectionHeaderContent = {
                    ContentSectionHeader(
                        sectionName = stringResource(id = R.string.top_billed_cast),
                        onSeeAllClick = onSeeAllCastClick
                    )
                },
                rowContent = {
                    items(
                        items = tvDetails.credits.cast.take(10),
                        key = { it.id }
                    ) {
                        CastItem(
                            id = it.id,
                            imagePath = it.profilePath,
                            name = it.name,
                            characterName = it.character,
                            onItemClick = onItemClick,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(140.dp)
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(140.dp)
                                .noRippleClickable { onSeeAllCastClick() }
                        ) {
                            Text(
                                text = stringResource(id = R.string.view_all),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .noRippleClickable { onSeeAllCastClick() }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(bottom = 2.dp)
            )

            OverviewSection(tvDetails.overview)

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

            LazyRowContentSection(
                pagingEnabled = false,
                sectionHeaderContent = {
                    Text(
                        text = stringResource(id = R.string.recommendations),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                rowContent = {
                    items(
                        items = tvDetails.recommendations,
                        key = { it.id }
                    ) {
                        MediaItemCard(
                            posterPath = it.imagePath,
                            onItemClick = { onItemClick("${it.id},${MediaType.TV}") },
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(110.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
        }
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