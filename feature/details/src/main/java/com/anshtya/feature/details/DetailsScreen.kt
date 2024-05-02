package com.anshtya.feature.details

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.details.MovieDetails
import com.anshtya.core.model.details.asLibraryItem
import com.anshtya.core.model.details.people.PersonDetails
import com.anshtya.core.model.details.tv.TvDetails
import com.anshtya.core.model.details.tv.asLibraryItem
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.core.ui.BackdropImage
import com.anshtya.core.ui.ContentSectionHeader
import com.anshtya.core.ui.FavoriteButton
import com.anshtya.core.ui.LazyRowContentSection
import com.anshtya.core.ui.MediaItemCard
import com.anshtya.core.ui.Rating
import com.anshtya.core.ui.WatchlistButton
import com.anshtya.core.ui.noRippleClickable
import kotlinx.coroutines.launch

private val horizontalPadding = 8.dp
private val verticalPadding = 4.dp

@Composable
internal fun DetailsRoute(
    onItemClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentDetailsUiState by viewModel.contentDetailsUiState.collectAsStateWithLifecycle()

    DetailsScreen(
        uiState = uiState,
        contentDetailsUiState = contentDetailsUiState,
        onErrorShown = viewModel::onErrorShown,
        onFavoriteClick = viewModel::addOrRemoveFavorite,
        onWatchlistClick = viewModel::addOrRemoveFromWatchlist,
        onItemClick = onItemClick,
        onSeeAllCastClick = onSeeAllCastClick,
        onBackClick = onBackClick
    )
}

@Composable
internal fun DetailsScreen(
    uiState: DetailsUiState,
    contentDetailsUiState: ContentDetailUiState,
    onErrorShown: () -> Unit,
    onFavoriteClick: (LibraryItem) -> Unit,
    onWatchlistClick: (LibraryItem) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            } else {
                when (contentDetailsUiState) {
                    ContentDetailUiState.Empty -> {
                        uiState.errorMessage?.let {
                            scope.launch { snackbarHostState.showSnackbar(it) }
                            onErrorShown()
                        }
                    }

                    is ContentDetailUiState.Movie -> {
                        MovieDetailsContent(
                            movieDetails = contentDetailsUiState.data,
                            onFavoriteClick = onFavoriteClick,
                            onWatchlistClick = onWatchlistClick,
                            onSeeAllCastClick = onSeeAllCastClick,
                            onItemClick = onItemClick
                        )
                    }

                    is ContentDetailUiState.TV -> {
                        TvDetailsContent(
                            tvDetails = contentDetailsUiState.data,
                            onFavoriteClick = onFavoriteClick,
                            onWatchlistClick = onWatchlistClick,
                            onSeeAllCastClick = onSeeAllCastClick,
                            onItemClick = onItemClick
                        )
                    }

                    is ContentDetailUiState.Person -> {
                        PersonDetailsContent(
                            personDetails = contentDetailsUiState.data
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movieDetails: MovieDetails,
    onFavoriteClick: (LibraryItem) -> Unit,
    onWatchlistClick: (LibraryItem) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val backdropHeight = 200.dp

        BackdropImageSection(
            path = movieDetails.backdropPath,
            modifier = Modifier
                .fillMaxWidth()
                .height(backdropHeight)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
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

            Row(
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                FavoriteButton(
                    active = false,
                    onClick = { onFavoriteClick(movieDetails.asLibraryItem()) }
                )
                Spacer(Modifier.width(6.dp))
                WatchlistButton(
                    active = false,
                    onClick = { onWatchlistClick(movieDetails.asLibraryItem()) }
                )
            }

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
                            onItemClick = { onItemClick("${it.id},${MediaType.MOVIE}") },
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
private fun TvDetailsContent(
    tvDetails: TvDetails,
    onFavoriteClick: (LibraryItem) -> Unit,
    onWatchlistClick: (LibraryItem) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val backdropHeight = 200.dp

        BackdropImageSection(
            path = tvDetails.backdropPath,
            modifier = Modifier
                .fillMaxWidth()
                .height(backdropHeight)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
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

            Row(
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                FavoriteButton(
                    active = false,
                    onClick = { onFavoriteClick(tvDetails.asLibraryItem()) }
                )
                Spacer(Modifier.width(6.dp))
                WatchlistButton(
                    active = false,
                    onClick = { onWatchlistClick(tvDetails.asLibraryItem()) }
                )
            }

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
private fun PersonDetailsContent(
    personDetails: PersonDetails
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = horizontalPadding, vertical = 6.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(Modifier.fillMaxWidth()) {
            MediaItemCard(
                personDetails.profilePath,
                modifier = Modifier.size(height = 200.dp, width = 140.dp)
            )

            Spacer(Modifier.width(10.dp))

            PersonInfoSection(
                name = personDetails.name,
                gender = personDetails.gender,
                birthday = personDetails.birthday,
                deathday = personDetails.deathday,
                department = personDetails.knownForDepartment
            )
        }

        PersonDetailsSection(
            alsoKnownAs = personDetails.alsoKnownAs,
            placeOfBirth = personDetails.placeOfBirth
        )

        OverviewSection(personDetails.biography)
    }
}

@Composable
private fun BackdropImageSection(
    path: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        BackdropImage(imageUrl = path)
    }
}

@Composable
private fun InfoSection(
    count: Int,
    genres: String,
    name: String,
    rating: Double,
    releaseYear: Int,
    tagline: String,
    modifier: Modifier = Modifier,
    runtime: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        Text(
            text = "$name ($releaseYear)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Rating(rating = rating, count = count)

        if (genres.isNotEmpty()) Text(genres)

        if (runtime.isNotEmpty()) Text(runtime)

        if (tagline.isNotEmpty()) {
            Text(
                text = tagline,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun OverviewSection(
    overview: String
) {
    Column {
        Text(
            text = stringResource(id = R.string.overview),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(2.dp))
        Text(overview)
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

@Composable
private fun PersonInfoSection(
    name: String,
    gender: String,
    birthday: String,
    deathday: String?,
    department: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        DetailItem(
            fieldName = stringResource(id = R.string.gender),
            value = gender
        )

        DetailItem(
            fieldName = stringResource(id = R.string.born),
            value = birthday
        )

        deathday?.let {
            DetailItem(
                fieldName = stringResource(id = R.string.died),
                value = it
            )
        }

        DetailItem(
            fieldName = stringResource(id = R.string.known_for),
            value = department
        )
    }
}

@Composable
private fun PersonDetailsSection(
    alsoKnownAs: String,
    placeOfBirth: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DetailItem(
            fieldName = stringResource(id = R.string.birth_place),
            value = placeOfBirth
        )
        DetailItem(
            fieldName = stringResource(id = R.string.also_known_as),
            value = alsoKnownAs
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

@Composable
private fun DetailItem(
    fieldName: String,
    value: String
) {
    val text = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append(fieldName)
        }
        append(value)
    }
    Text(text)
}

@Composable
private fun CastItem(
    id: Int,
    imagePath: String,
    name: String,
    characterName: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        shadowElevation = 4.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onItemClick("${id},${MediaType.PERSON}") }
        ) {
            MediaItemCard(
                posterPath = imagePath,
                onItemClick = { onItemClick("${id},${MediaType.PERSON}") },
                modifier = Modifier.height(160.dp)
            )
            Spacer(Modifier.height(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 2.dp, horizontal = 4.dp)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = characterName,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}