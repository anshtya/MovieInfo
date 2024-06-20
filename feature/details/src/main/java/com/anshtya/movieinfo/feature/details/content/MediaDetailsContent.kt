package com.anshtya.movieinfo.feature.details.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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
import com.anshtya.movieinfo.feature.details.OverviewSection
import com.anshtya.movieinfo.feature.details.R
import com.anshtya.movieinfo.feature.details.horizontalPadding
import com.anshtya.movieinfo.feature.details.verticalPadding

private val backdropExpandedHeight = 220.dp
private val collapsedHeight = 64.dp
private val collapseHeight = backdropExpandedHeight - collapsedHeight

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
    onBackdropCollapse: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val collapseHeightPx = with(LocalDensity.current) { collapseHeight.toPx() }
    val nestedScrollConnection = remember(collapseHeightPx) {
        ExitOnlyCollapseNestedConnection(collapseHeightPx)
    }
    val backdropHeight = with(LocalDensity.current) {
        (backdropExpandedHeight.toPx() + nestedScrollConnection.collapseOffsetHeightPx).toDp()
    }

    val isBackdropCollapsed by remember(backdropHeight) {
        derivedStateOf { backdropHeight == collapsedHeight }
    }
    LaunchedEffect(isBackdropCollapsed) {
        onBackdropCollapse(isBackdropCollapsed)
    }

    val scrollValue = 1 - ((backdropExpandedHeight - backdropHeight) / collapseHeight)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .nestedScroll(nestedScrollConnection)
    ) {
        BackdropImageSection(
            path = backdropPath,
            scrollValue = scrollValue,
            modifier = Modifier.height(backdropHeight)
        )
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                InfoSection(
                    voteCount = voteCount,
                    name = name,
                    rating = rating,
                    releaseYear = releaseYear,
                    runtime = runtime,
                    tagline = tagline
                )
            }

            item { GenreSection(genres) }

            item {
                LibraryActions(
                    isFavorite = isFavorite,
                    isAddedToWatchList = isAddedToWatchList,
                    onFavoriteClick = onFavoriteClick,
                    onWatchlistClick = onWatchlistClick
                )
            }

            item {
                TopBilledCast(
                    cast = cast,
                    onCastClick = onCastClick,
                    onSeeAllCastClick = onSeeAllCastClick
                )
            }

            item { OverviewSection(overview) }

            item { content() }

            item {
                Recommendations(
                    recommendations = recommendations,
                    onRecommendationClick = onRecommendationClick
                )
            }
        }
    }
}

private class ExitOnlyCollapseNestedConnection(
    val collapseHeightPx: Float
) : NestedScrollConnection {
    var collapseOffsetHeightPx by mutableFloatStateOf(0f)
        private set

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y

        // if scrolling down, don't consume anything
        if (delta > 0f) return Offset.Zero

        val previousOffset = collapseOffsetHeightPx
        val newOffset = collapseOffsetHeightPx + delta
        collapseOffsetHeightPx = newOffset.coerceIn(-collapseHeightPx, 0f)
        return if (previousOffset != collapseOffsetHeightPx) {
            // We are in the middle of top app bar collapse
            available
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        // change height of top app bar when scrolling all the way down and
        // child has finished scrolling
        if (consumed.y >= 0f && available.y > 0f) {
            val prevOffset = collapseOffsetHeightPx
            val newOffset = collapseOffsetHeightPx + available.y
            collapseOffsetHeightPx = newOffset.coerceIn(-collapseHeightPx, 0f)
            return Offset(x = 0f, y = (collapseOffsetHeightPx - prevOffset))
        }

        return Offset.Zero
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
private fun BackdropImageSection(
    path: String,
    scrollValue: Float,
    modifier: Modifier = Modifier
) {
    Surface(modifier.fillMaxWidth()) {
        TmdbImage(
            width = 1280,
            imageUrl = path,
            contentScale = ContentScale.Crop,
            alpha = scrollValue
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InfoSection(
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
            textAlign = TextAlign.Center,
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
private fun GenreSection(
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
private fun TopBilledCast(
    cast: List<Cast>,
    onCastClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit,
) {
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
}

@Composable
private fun Recommendations(
    recommendations: List<ContentItem>,
    onRecommendationClick: (String) -> Unit
) {
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

@Composable
private fun CastItem(
    id: Int,
    imagePath: String,
    name: String,
    characterName: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(6.dp)
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
private fun LibraryActions(
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
            name = if (isFavorite) {
                stringResource(id = R.string.remove_from_favorites)
            } else {
                stringResource(id = R.string.add_to_favorites)
            },
            icon = Icons.Rounded.Favorite,
            iconTint = if (isFavorite) {
                Color.Red
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            onClick = onFavoriteClick,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        LibraryActionButton(
            name = if (isAddedToWatchList) {
                stringResource(id = R.string.remove_from_watchlist)
            } else {
                stringResource(id = R.string.add_to_watchlist)
            },
            icon = if (isAddedToWatchList) {
                Icons.Rounded.Bookmark
            } else {
                Icons.Outlined.BookmarkBorder
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(1.dp, Color.Black),
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
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(8.dp)
        )
    }
}