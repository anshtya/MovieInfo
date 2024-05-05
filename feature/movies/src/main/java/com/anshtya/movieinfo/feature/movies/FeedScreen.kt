package com.anshtya.movieinfo.feature.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.content.MovieListCategory
import com.anshtya.core.ui.ContentSectionHeader
import com.anshtya.core.ui.LazyRowContentSection
import com.anshtya.core.ui.MediaItemCard
import kotlinx.coroutines.launch

private val horizontalPadding = 8.dp

@Composable
internal fun FeedRoute(
    navigateToDetails: (String) -> Unit,
    navigateToItems: (String) -> Unit,
    viewModel: MoviesViewModel
) {
    val nowPlayingMovies by viewModel.nowPlayingMovies.collectAsStateWithLifecycle()
    val popularMovies by viewModel.popularMovies.collectAsStateWithLifecycle()
    val topRatedMovies by viewModel.topRatedMovies.collectAsStateWithLifecycle()
    val upcomingMovies by viewModel.upcomingMovies.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    FeedScreen(
        nowPlayingMovies = nowPlayingMovies,
        popularMovies = popularMovies,
        topRatedMovies = topRatedMovies,
        upcomingMovies = upcomingMovies,
        errorMessage = errorMessage,
        appendItems = viewModel::appendItems,
        onItemClick = navigateToDetails,
        onSeeAllClick = navigateToItems,
        onErrorShown = viewModel::onErrorShown
    )
}

@Composable
internal fun FeedScreen(
    nowPlayingMovies: ContentUiState,
    popularMovies: ContentUiState,
    topRatedMovies: ContentUiState,
    upcomingMovies: ContentUiState,
    errorMessage: String?,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
    onErrorShown: () -> Unit
) {
    val scrollState = rememberScrollState()
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    errorMessage?.let {
        scope.launch { snackbarState.showSnackbar(it) }
        onErrorShown()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(top = 4.dp, bottom = 8.dp)
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            NowPlayingMovies(
                content = nowPlayingMovies,
                onItemClick = onItemClick,
                appendItems = appendItems,
                onSeeAllClick = onSeeAllClick
            )
            PopularMovies(
                content = popularMovies,
                onItemClick = onItemClick,
                appendItems = appendItems,
                onSeeAllClick = onSeeAllClick
            )
            TopRatedMovies(
                content = topRatedMovies,
                onItemClick = onItemClick,
                appendItems = appendItems,
                onSeeAllClick = onSeeAllClick
            )
            UpcomingMovies(
                content = upcomingMovies,
                onItemClick = onItemClick,
                appendItems = appendItems,
                onSeeAllClick = onSeeAllClick
            )
        }
    }
}

@Composable
private fun NowPlayingMovies(
    content: ContentUiState,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    ContentSection(
        content = content,
        sectionName = stringResource(id = R.string.now_playing),
        appendItems = appendItems,
        onItemClick = onItemClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
private fun PopularMovies(
    content: ContentUiState,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    ContentSection(
        content = content,
        sectionName = stringResource(id = R.string.popular),
        appendItems = appendItems,
        onItemClick = onItemClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
private fun TopRatedMovies(
    content: ContentUiState,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    ContentSection(
        content = content,
        sectionName = stringResource(id = R.string.top_rated),
        appendItems = appendItems,
        onItemClick = onItemClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
private fun UpcomingMovies(
    content: ContentUiState,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    ContentSection(
        content = content,
        sectionName = stringResource(id = R.string.upcoming),
        appendItems = appendItems,
        onItemClick = onItemClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
private fun ContentSection(
    content: ContentUiState,
    sectionName: String,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    LazyRowContentSection(
        pagingEnabled = true,
        isLoading = content.isLoading,
        endReached = content.endReached,
        itemsEmpty = content.items.isEmpty(),
        rowContentPadding = PaddingValues(horizontal = horizontalPadding),
        appendItems = { appendItems(content.category) },
        sectionHeaderContent = {
            ContentSectionHeader(
                sectionName = sectionName,
                onSeeAllClick = { onSeeAllClick(content.category.name) },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )
        },
        rowContent = {
            items(
                items = content.items,
                key = { it.id }
            ) {
                MediaItemCard(
                    posterPath = it.imagePath,
                    onItemClick = {
                        onItemClick("${it.id},${MediaType.MOVIE}")
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(110.dp)
                )
            }

            if (content.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(110.dp)
                    ) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    )
}