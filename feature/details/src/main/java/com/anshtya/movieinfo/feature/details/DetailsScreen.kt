package com.anshtya.movieinfo.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.library.LibraryItem
import com.anshtya.movieinfo.core.ui.LibraryActionButton
import com.anshtya.movieinfo.core.ui.Rating
import com.anshtya.movieinfo.core.ui.TmdbImage
import com.anshtya.movieinfo.core.ui.noRippleClickable
import com.anshtya.movieinfo.feature.details.content.MovieDetailsContent
import com.anshtya.movieinfo.feature.details.content.PersonDetailsContent
import com.anshtya.movieinfo.feature.details.content.TvShowDetailsContent
import kotlinx.coroutines.launch

private val horizontalPadding = 8.dp
private val verticalPadding = 4.dp
private val backdropHeight = 220.dp

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
            val signInSheetContentDescription = stringResource(
                id = R.string.details_sign_in_sheet
            )
            if (uiState.showSignInSheet) {
                ModalBottomSheet(
                    onDismissRequest = onHideBottomSheet,
                    sheetState = bottomSheetState,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.semantics {
                        contentDescription = signInSheetContentDescription
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
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
            when (contentDetailsUiState) {
                ContentDetailUiState.Loading -> {
                    val loadingContentDescription = stringResource(
                        id = R.string.details_loading
                    )
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .semantics {
                                contentDescription = loadingContentDescription
                            }
                    )
                }

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
                        onBackClick = onBackClick,
                        modifier = Modifier.padding(
                            horizontal = horizontalPadding,
                            vertical = 6.dp
                        )
                    )
                }
            }

            if (contentDetailsUiState !is ContentDetailUiState.Person) {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = stringResource(
                                    id = com.anshtya.movieinfo.core.ui.R.string.back
                                ),
                                tint = Color.White,
                                modifier = Modifier
                                    .noRippleClickable { onBackClick() }
                                    .padding(6.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
internal fun BackdropImageSection(
    path: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(
            bottomEnd = 18.dp,
            bottomStart = 18.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(backdropHeight)
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
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier
                .width(120.dp)
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
                    .padding(vertical = 2.dp, horizontal = 4.dp)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = characterName,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
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
            modifier = Modifier.padding(4.dp)
        )
    }
}