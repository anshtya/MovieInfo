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
import com.anshtya.movieinfo.core.model.details.MovieDetails
import com.anshtya.movieinfo.core.model.library.LibraryItem
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
import com.anshtya.movieinfo.feature.details.R
import com.anshtya.movieinfo.feature.details.backdropHeight

@Composable
internal fun MovieDetailsContent(
    movieDetails: MovieDetails,
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
            path = movieDetails.backdropPath,
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
                    posterPath = movieDetails.posterPath,
                    modifier = Modifier.size(height = 200.dp, width = 140.dp)
                )
                Spacer(Modifier.width(10.dp))
                InfoSection(
                    count = movieDetails.voteCount,
                    genres = movieDetails.genres,
                    name = movieDetails.title,
                    rating = movieDetails.rating,
                    releaseYear = movieDetails.releaseYear,
                    runtime = movieDetails.runtime,
                    tagline = movieDetails.tagline
                )
            }

            LibraryActions(
                isFavorite = isFavorite,
                isAddedToWatchList = isAddedToWatchList,
                onFavoriteClick = { onFavoriteClick(movieDetails.asLibraryItem()) },
                onWatchlistClick = { onWatchlistClick(movieDetails.asLibraryItem() )}
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
                        items = movieDetails.credits.cast.take(10),
                        key = { it.id }
                    ) {
                        CastItem(
                            id = it.id,
                            imagePath = it.profilePath,
                            name = it.name,
                            characterName = it.character,
                            onItemClick = onItemClick
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
                    .height(200.dp)
                    .padding(bottom = 2.dp)
            )

            OverviewSection(movieDetails.overview)

            MovieDetailsSection(
                releaseDate = movieDetails.releaseDate,
                originalLanguage = movieDetails.originalLanguage,
                productionCompanies = movieDetails.productionCompanies,
                productionCountries = movieDetails.productionCountries,
                budget = movieDetails.budget,
                revenue = movieDetails.revenue
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
                        items = movieDetails.recommendations,
                        key = { it.id }
                    ) {
                        MediaItemCard(
                            posterPath = it.imagePath,
                            onItemClick = { onItemClick("${it.id},${MediaType.MOVIE}") }
                        )
                    }
                },
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
private fun MovieDetailsSection(
    releaseDate: String,
    originalLanguage: String,
    productionCompanies: String,
    productionCountries: String,
    budget: String,
    revenue: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        DetailItem(
            fieldName = stringResource(id = R.string.release_date),
            value = releaseDate
        )

        DetailItem(
            fieldName = stringResource(id = R.string.original_language),
            value = originalLanguage
        )

        DetailItem(
            fieldName = stringResource(id = R.string.budget),
            value = "$${budget}"
        )

        DetailItem(
            fieldName = stringResource(id = R.string.revenue),
            value = "$${revenue}"
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