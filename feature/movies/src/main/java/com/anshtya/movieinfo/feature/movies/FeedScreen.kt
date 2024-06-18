package com.anshtya.movieinfo.feature.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.content.MovieListCategory
import com.anshtya.movieinfo.core.ui.ContentSectionHeader
import com.anshtya.movieinfo.core.ui.LazyRowContentSection
import com.anshtya.movieinfo.core.ui.MediaItemCard
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
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    errorMessage?.let {
        scope.launch { snackbarState.showSnackbar(it) }
        onErrorShown()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(top = 4.dp, bottom = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ContentSection(
                    content = nowPlayingMovies,
                    sectionName = stringResource(id = R.string.now_playing),
                    appendItems = appendItems,
                    onItemClick = onItemClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
            item {
                ContentSection(
                    content = popularMovies,
                    sectionName = stringResource(id = R.string.popular),
                    appendItems = appendItems,
                    onItemClick = onItemClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
            item {
                ContentSection(
                    content = topRatedMovies,
                    sectionName = stringResource(id = R.string.top_rated),
                    appendItems = appendItems,
                    onItemClick = onItemClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
            item {
                ContentSection(
                    content = upcomingMovies,
                    sectionName = stringResource(id = R.string.upcoming),
                    appendItems = appendItems,
                    onItemClick = onItemClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
        }
    }
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
                    }
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
        },
        modifier = Modifier.height(160.dp)
    )
}