package com.anshtya.movieinfo.feature.movies

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.content.ContentItem
import com.anshtya.core.model.content.MovieListCategory
import com.anshtya.core.ui.MediaItemCard
import com.anshtya.core.ui.noRippleClickable

private val horizontalPadding = 8.dp

@Composable
internal fun FeedRoute(
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
    viewModel: MoviesViewModel
) {
    val nowPlayingMovies = viewModel.nowPlayingMovies.collectAsLazyPagingItems()
    val popularMovies = viewModel.popularMovies.collectAsLazyPagingItems()
    val topRatedMovies = viewModel.topRatedMovies.collectAsLazyPagingItems()
    val upcomingMovies = viewModel.upcomingMovies.collectAsLazyPagingItems()

    FeedScreen(
        nowPlayingMovies = LazyPagingContent(nowPlayingMovies),
        popularMovies = LazyPagingContent(popularMovies),
        topRatedMovies = LazyPagingContent(topRatedMovies),
        upcomingMovies = LazyPagingContent(upcomingMovies),
        onItemClick = onItemClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
internal fun FeedScreen(
    nowPlayingMovies: LazyPagingContent,
    popularMovies: LazyPagingContent,
    topRatedMovies: LazyPagingContent,
    upcomingMovies: LazyPagingContent,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    Surface {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            nowPlayingMovies(
                content = nowPlayingMovies,
                onItemClick = onItemClick,
                onSeeAllClick = onSeeAllClick
            )
            popularMovies(
                content = popularMovies,
                onItemClick = onItemClick,
                onSeeAllClick = onSeeAllClick
            )
            topRatedMovies(
                content = topRatedMovies,
                onItemClick = onItemClick,
                onSeeAllClick = onSeeAllClick
            )
            upcomingMovies(
                content = upcomingMovies,
                onItemClick = onItemClick,
                onSeeAllClick = onSeeAllClick
            )
        }
    }
}

private fun LazyListScope.nowPlayingMovies(
    content: LazyPagingContent,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    item {
        ContentSection(
            content = content,
            sectionName = R.string.now_playing,
            onItemClick = onItemClick,
            onSeeAllClick = {
                onSeeAllClick(MovieListCategory.NOW_PLAYING.name)
            }
        )
    }
}

private fun LazyListScope.popularMovies(
    content: LazyPagingContent,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    item {
        ContentSection(
            content = content,
            sectionName = R.string.popular,
            onItemClick = onItemClick,
            onSeeAllClick = {
                onSeeAllClick(MovieListCategory.POPULAR.name)
            }
        )
    }
}

private fun LazyListScope.topRatedMovies(
    content: LazyPagingContent,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    item {
        ContentSection(
            content = content,
            sectionName = R.string.top_rated,
            onItemClick = onItemClick,
            onSeeAllClick = {
                onSeeAllClick(MovieListCategory.TOP_RATED.name)
            }
        )
    }
}

private fun LazyListScope.upcomingMovies(
    content: LazyPagingContent,
    onItemClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    item {
        ContentSection(
            content = content,
            sectionName = R.string.upcoming,
            onItemClick = onItemClick,
            onSeeAllClick = {
                onSeeAllClick(MovieListCategory.UPCOMING.name)
            }
        )
    }
}

@Composable
private fun ContentSection(
    content: LazyPagingContent,
    @StringRes sectionName: Int,
    onItemClick: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    val lazyRowState = rememberLazyListState()

    val isLoading by remember(content.items.loadState.refresh) {
        derivedStateOf { content.items.loadState.refresh is LoadState.Loading }
    }

    LaunchedEffect(isLoading) {
        if (isLoading) lazyRowState.scrollToItem(0)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = sectionName),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )


            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = stringResource(id = R.string.see_all),
                modifier = Modifier.noRippleClickable { onSeeAllClick() }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    state = lazyRowState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = content.items.itemCount,
                        key = content.items.itemKey { it.id }
                    ) { index ->
                        content.items[index]?.let { contentItem ->
                            MediaItemCard(
                                posterPath = contentItem.imagePath,
                                onItemClick = {
                                    onItemClick("${contentItem.id},${MediaType.MOVIE}")
                                },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(110.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Stable
internal data class LazyPagingContent(
    val items: LazyPagingItems<ContentItem>
)