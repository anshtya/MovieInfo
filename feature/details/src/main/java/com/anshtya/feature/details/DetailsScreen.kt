package com.anshtya.feature.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.core.ui.BackdropImage
import com.anshtya.core.ui.LibraryActionButton
import com.anshtya.core.ui.MediaItemCard
import com.anshtya.core.ui.Rating
import com.anshtya.feature.details.content.MovieDetailsContent
import com.anshtya.feature.details.content.PersonDetailsContent
import com.anshtya.feature.details.content.TvShowDetailsContent
import kotlinx.coroutines.launch

private val horizontalPadding = 8.dp
private val verticalPadding = 4.dp
internal val backdropHeight = 200.dp

@Composable
internal fun DetailsRoute(
    onItemClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit,
    navigateToAuth: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentDetailsUiState by viewModel.contentDetailsUiState.collectAsStateWithLifecycle()

    DetailsScreen(
        uiState = uiState,
        contentDetailsUiState = contentDetailsUiState,
        onHideBottomSheet = viewModel::onHideBottomSheet,
        onErrorShown = viewModel::onErrorShown,
        onFavoriteClick = viewModel::addOrRemoveFavorite,
        onWatchlistClick = viewModel::addOrRemoveFromWatchlist,
        onItemClick = onItemClick,
        onSeeAllCastClick = onSeeAllCastClick,
        onSignInClick = navigateToAuth,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsScreen(
    uiState: DetailsUiState,
    contentDetailsUiState: ContentDetailUiState,
    onHideBottomSheet: () -> Unit,
    onErrorShown: () -> Unit,
    onFavoriteClick: (LibraryItem) -> Unit,
    onWatchlistClick: (LibraryItem) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(uiState.showSignInSheet) {
        if (uiState.showSignInSheet) {
            bottomSheetState.show()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        sheetContent = {
            if (uiState.showSignInSheet) {
                ModalBottomSheet(
                    onDismissRequest = onHideBottomSheet,
                    sheetState = bottomSheetState,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_in_sheet_text),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(Modifier.height(50.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    bottomSheetState.hide()
                                }.invokeOnCompletion {
                                    onHideBottomSheet()
                                }
                                onSignInClick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(text = stringResource(id = R.string.sign_in))
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
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
                            isFavorite = uiState.markedFavorite,
                            isAddedToWatchList = uiState.savedInWatchlist,
                            onFavoriteClick = onFavoriteClick,
                            onWatchlistClick = onWatchlistClick,
                            onSeeAllCastClick = onSeeAllCastClick,
                            onItemClick = onItemClick,
                            modifier = Modifier.padding(
                                horizontal = horizontalPadding,
                                vertical = verticalPadding
                            )
                        )
                    }

                    is ContentDetailUiState.TV -> {
                        TvShowDetailsContent(
                            tvDetails = contentDetailsUiState.data,
                            isFavorite = uiState.markedFavorite,
                            isAddedToWatchList = uiState.savedInWatchlist,
                            onFavoriteClick = onFavoriteClick,
                            onWatchlistClick = onWatchlistClick,
                            onSeeAllCastClick = onSeeAllCastClick,
                            onItemClick = onItemClick,
                            modifier = Modifier.padding(
                                horizontal = horizontalPadding,
                                vertical = verticalPadding
                            )
                        )
                    }

                    is ContentDetailUiState.Person -> {
                        PersonDetailsContent(
                            personDetails = contentDetailsUiState.data,
                            modifier = Modifier.padding(
                                horizontal = horizontalPadding,
                                vertical = 6.dp
                            )
                        )
                    }
                }
            }
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
        modifier = modifier
    ) {
        BackdropImage(imageUrl = path)
    }
}

@Composable
internal fun InfoSection(
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
        Text(overview)
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
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
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