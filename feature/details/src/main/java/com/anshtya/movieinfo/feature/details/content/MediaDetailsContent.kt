package com.anshtya.movieinfo.feature.details.content

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.content.ContentItem
import com.anshtya.movieinfo.core.model.details.people.Cast
import com.anshtya.movieinfo.core.ui.ContentSectionHeader
import com.anshtya.movieinfo.core.ui.LazyRowContentSection
import com.anshtya.movieinfo.core.ui.LibraryActionButton
import com.anshtya.movieinfo.core.ui.MediaItemCard
import com.anshtya.movieinfo.core.ui.Rating
import com.anshtya.movieinfo.core.ui.TmdbImage
import com.anshtya.movieinfo.core.ui.noRippleClickable
import com.anshtya.movieinfo.feature.details.R
import com.anshtya.movieinfo.feature.details.horizontalPadding
import com.anshtya.movieinfo.feature.details.verticalPadding

private val backdropHeight = 220.dp

@Composable
internal fun MediaDetailsContent(
    backdropPath: String,
    voteCount: Int,
    name: String,
    rating: Double,
    releaseYear: Int,
    runtime: String,
    tagline: String,
    genres: List<String>,
    overview: String,
    cast: List<Cast>,
    recommendations: List<ContentItem>,
    isFavorite: Boolean,
    isAddedToWatchList: Boolean,
    onFavoriteClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onSeeAllCastClick: () -> Unit,
    onCastClick: (String) -> Unit,
    onRecommendationClick: (String) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        BackdropImageSection(
            path = backdropPath,
            modifier = Modifier.height(backdropHeight)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                )
        ) {
            InfoSection(
                voteCount = voteCount,
                name = name,
                rating = rating,
                releaseYear = releaseYear,
                runtime = runtime,
                tagline = tagline
            )

            GenreSection(genres)

            LibraryActions(
                isFavorite = isFavorite,
                isAddedToWatchList = isAddedToWatchList,
                onFavoriteClick = onFavoriteClick,
                onWatchlistClick = onWatchlistClick
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ContentSectionHeader(
                    sectionName = stringResource(id = R.string.top_billed_cast),
                    onSeeAllClick = onSeeAllCastClick
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .height(IntrinsicSize.Max)
                        .padding(bottom = 2.dp)
                ) {
                    cast.forEach {
                        CastItem(
                            id = it.id,
                            imagePath = it.profilePath,
                            name = it.name,
                            characterName = it.character,
                            onItemClick = onCastClick
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(130.dp)
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
            }

            OverviewSection(overview)

            content()

            LazyRowContentSection(
                pagingEnabled = false,
                sectionHeaderContent = {
                    Text(
                        text = stringResource(id = R.string.recommendations),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                rowContent = {
                    if (recommendations.isEmpty()) {
                        item {
                            Box(Modifier.fillMaxSize()) {
                                Text(
                                    text = stringResource(id = R.string.not_available),
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    } else {
                        items(
                            items = recommendations,
                            key = { it.id }
                        ) {
                            MediaItemCard(
                                posterPath = it.imagePath,
                                onItemClick = { onRecommendationClick("${it.id}") }
                            )
                        }
                    }
                },
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
internal fun BackdropImageSection(
    path: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.fillMaxWidth()
    ) {
        TmdbImage(
            width = 1280,
            imageUrl = path
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun InfoSection(
    voteCount: Int,
    name: String,
    rating: Double,
    releaseYear: Int,
    tagline: String,
    runtime: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (runtime.isNotEmpty()) {
                Text(runtime)
                if (releaseYear.toString().isNotEmpty()) {
                    Text("|")
                    Text("$releaseYear")
                }
            } else {
                Text("$releaseYear")
            }
        }

        Rating(rating = rating, count = voteCount)

        if (tagline.isNotEmpty()) {
            Text(
                text = tagline,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GenreSection(
    genres: List<String>
) {
    if (genres.isNotEmpty()) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach {
                GenreButton(name = it)
            }
        }
    }
}

@Composable
internal fun OverviewSection(
    overview: String
) {
    Column {
        Text(
            text = stringResource(id = R.string.overview),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(2.dp))
        if (overview.isEmpty()) {
            Text(text = stringResource(id = R.string.not_available))
        } else {
            Text(overview)
        }
    }
}

@Composable
internal fun DetailItem(
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
internal fun CastItem(
    id: Int,
    imagePath: String,
    name: String,
    characterName: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(130.dp)
                .noRippleClickable {
                    onItemClick("${id},${MediaType.PERSON}")
                }
        ) {
            TmdbImage(
                width = 500,
                imageUrl = imagePath,
                modifier = modifier
                    .height(140.dp)
                    .noRippleClickable {
                        onItemClick("${id},${MediaType.PERSON}")
                    }
            )
            Spacer(Modifier.height(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(2.dp))
                Text(text = characterName)
            }
        }
    }
}

@Composable
internal fun LibraryActions(
    isFavorite: Boolean,
    isAddedToWatchList: Boolean,
    onFavoriteClick: () -> Unit,
    onWatchlistClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(top = 6.dp, bottom = 4.dp)
    ) {
        LibraryActionButton(
            active = isFavorite,
            name = stringResource(id = R.string.mark_favorite),
            icon = Icons.Rounded.Favorite,
            onClick = onFavoriteClick,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        LibraryActionButton(
            active = isAddedToWatchList,
            name = stringResource(id = R.string.add_to_watchlist),
            icon = Icons.Rounded.Bookmark,
            onClick = onWatchlistClick,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
    }
}

@Composable
private fun GenreButton(
    name: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(8.dp)
        )
    }
}