package com.anshtya.feature.details

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.core.model.MovieDetails
import com.anshtya.core.model.PersonDetails
import com.anshtya.core.model.TvDetails
import com.anshtya.core.ui.BackdropImage
import com.anshtya.core.ui.FavoriteButton
import com.anshtya.core.ui.Rating
import com.anshtya.core.ui.TmdbImage
import com.anshtya.core.ui.WatchlistButton

private val horizontalPadding = 10.dp

@Composable
internal fun DetailsRoute(
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentDetailsUiState by viewModel.contentDetailsUiState.collectAsStateWithLifecycle()

    DetailsScreen(
        uiState = uiState,
        contentDetailsUiState = contentDetailsUiState,
        onErrorShown = viewModel::onErrorShown
    )
}

@Composable
internal fun DetailsScreen(
    uiState: DetailsUiState,
    contentDetailsUiState: ContentDetailUiState,
    onErrorShown: () -> Unit
) {
    val context = LocalContext.current

    Column(Modifier.fillMaxSize()) {

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else {
            when (contentDetailsUiState) {
                ContentDetailUiState.Empty -> {
                    uiState.errorMessage?.let {
                        Toast.makeText(context, it.toText(context), Toast.LENGTH_SHORT).show()
                        onErrorShown()
                    }
                }

                is ContentDetailUiState.Movie -> {
                    MovieDetailsContent(movieDetails = contentDetailsUiState.data)
                }

                is ContentDetailUiState.TV -> {
                    TvDetailsContent(tvDetails = contentDetailsUiState.data)
                }

                is ContentDetailUiState.Person -> {
                    PersonDetailsContent(personDetails = contentDetailsUiState.data)
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movieDetails: MovieDetails
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
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Poster(
                    path = movieDetails.posterPath,
                    modifier = Modifier
                        .size(height = 200.dp, width = 140.dp)
                        .offset(y = (-20).dp)
                )
                Spacer(Modifier.width(6.dp))
                InfoSection(
                    count = movieDetails.voteCount,
                    genres = movieDetails.genres,
                    name = movieDetails.title,
                    rating = movieDetails.rating,
                    releaseYear = movieDetails.releaseYear,
                    runtime = movieDetails.runtime,
                    tagline = movieDetails.tagline,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Row {
                FavoriteButton(active = false, onClick = {})

                Spacer(Modifier.width(6.dp))

                WatchlistButton(active = false, onClick = {})
            }

            OverviewSection(movieDetails.overview)

            Spacer(Modifier.height(4.dp))

            MovieDetailsSection(
                releaseDate = movieDetails.releaseDate,
                originalLanguage = movieDetails.originalLanguage,
                productionCompanies = movieDetails.productionCompanies,
                productionCountries = movieDetails.productionCountries,
                budget = movieDetails.budget,
                revenue = movieDetails.revenue
            )
        }
    }
}

@Composable
private fun TvDetailsContent(
    tvDetails: TvDetails
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
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Poster(
                    path = tvDetails.posterPath,
                    modifier = Modifier
                        .size(height = 200.dp, width = 140.dp)
                        .offset(y = (-20).dp)
                )

                Spacer(Modifier.width(6.dp))

                InfoSection(
                    count = tvDetails.voteCount,
                    genres = tvDetails.genres,
                    name = tvDetails.name,
                    rating = tvDetails.rating,
                    releaseYear = tvDetails.releaseYear,
                    tagline = tvDetails.tagline,
                    runtime = tvDetails.episodeRunTime,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Row {
                FavoriteButton(active = false, onClick = {})

                Spacer(Modifier.width(6.dp))

                WatchlistButton(active = false, onClick = {})
            }

            OverviewSection(tvDetails.overview)

            Spacer(Modifier.height(4.dp))

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
}

@Composable
private fun PersonDetailsContent(
    personDetails: PersonDetails
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = horizontalPadding, vertical = 6.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(Modifier.fillMaxWidth()) {
            Poster(personDetails.profilePath)

            Spacer(Modifier.width(6.dp))

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
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    ) {
        BackdropImage(imageUrl = path)
    }
}

@Composable
private fun Poster(
    path: String,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            TmdbImage(imageUrl = path)
        }
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

        Text(genres)

        Text(runtime)

        Text(
            text = tagline,
            fontStyle = FontStyle.Italic
        )
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
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.release_date),
                fontWeight = FontWeight.SemiBold
            )
            Text(releaseDate)
        }

        Row {
            Text(
                text = stringResource(id = R.string.original_language),
                fontWeight = FontWeight.SemiBold
            )
            Text(originalLanguage)
        }

        Row {
            Text(
                text = stringResource(id = R.string.budget),
                fontWeight = FontWeight.SemiBold
            )
            Text("$${budget}")
        }

        Row {
            Text(
                text = stringResource(id = R.string.revenue),
                fontWeight = FontWeight.SemiBold
            )
            Text("$${revenue}")
        }

        Row {
            Text(
                text = stringResource(id = R.string.production_companies),
                fontWeight = FontWeight.SemiBold
            )
            Text(productionCompanies)
        }

        Row {
            Text(
                text = stringResource(id = R.string.production_countries),
                fontWeight = FontWeight.SemiBold
            )
            Text(productionCountries)
        }
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

        Row {
            Text(
                text = stringResource(id = R.string.gender),
                fontWeight = FontWeight.SemiBold
            )
            Text(gender)
        }

        Row {
            Text(
                text = stringResource(id = R.string.born),
                fontWeight = FontWeight.SemiBold
            )
            Text(birthday)
        }

        deathday?.let {
            Row {
                Text(
                    text = stringResource(id = R.string.died),
                    fontWeight = FontWeight.SemiBold
                )
                Text(it)
            }
        }

        Row {
            Text(
                text = stringResource(id = R.string.known_for),
                fontWeight = FontWeight.SemiBold
            )
            Text(department)
        }
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
        Row {
            Text(
                text = stringResource(id = R.string.birth_place),
                fontWeight = FontWeight.SemiBold
            )
            Text(placeOfBirth)
        }
        Row {
            Text(
                text = stringResource(id = R.string.also_known_as),
                fontWeight = FontWeight.SemiBold
            )
            Text(alsoKnownAs)
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
        Row {
            Text(
                text = stringResource(id = R.string.original_language),
                fontWeight = FontWeight.SemiBold
            )
            Text(originalLanguage)
        }

        Row {
            Text(
                text = stringResource(id = R.string.first_air_date),
                fontWeight = FontWeight.SemiBold
            )
            Text(firstAirDate)
        }

        Row {
            Text(
                text = stringResource(id = R.string.last_air_date),
                fontWeight = FontWeight.SemiBold
            )
            Text(lastAirDate)
        }

        Row {
            Text(
                text = stringResource(id = R.string.in_production),
                fontWeight = FontWeight.SemiBold
            )
            Text(inProduction)
        }

        Row {
            Text(
                text = stringResource(id = R.string.status),
                fontWeight = FontWeight.SemiBold
            )
            Text(status)
        }

        nextAirDate?.let {
            Row {
                Text(
                    text = stringResource(id = R.string.next_air_date),
                    fontWeight = FontWeight.SemiBold
                )
                Text(it)
            }
        }

        Row {
            Text(
                text = stringResource(id = R.string.number_episodes),
                fontWeight = FontWeight.SemiBold
            )
            Text("$numberOfEpisodes")
        }

        Row {
            Text(
                text = stringResource(id = R.string.number_seasons),
                fontWeight = FontWeight.SemiBold
            )
            Text("$numberOfSeasons")
        }

        Row {
            Text(
                text = stringResource(id = R.string.networks),
                fontWeight = FontWeight.SemiBold
            )
            Text(networks)
        }

        Row {
            Text(
                text = stringResource(id = R.string.production_companies),
                fontWeight = FontWeight.SemiBold
            )
            Text(productionCompanies)
        }

        Row {
            Text(
                text = stringResource(id = R.string.production_countries),
                fontWeight = FontWeight.SemiBold
            )
            Text(productionCountries)
        }
    }
}