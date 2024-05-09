package com.anshtya.movieinfo.feature.movies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.content.MovieListCategory
import com.anshtya.movieinfo.core.ui.MediaItemCard
import com.anshtya.movieinfo.core.ui.LazyVerticalContentGrid
import com.anshtya.movieinfo.core.ui.noRippleClickable

private val horizontalPadding = 8.dp

@Composable
fun ItemsRoute(
    categoryName: String,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: MoviesViewModel
) {
    val category = enumValueOf<MovieListCategory>(categoryName)
    val content by when (category) {
        MovieListCategory.NOW_PLAYING -> viewModel.nowPlayingMovies.collectAsStateWithLifecycle()
        MovieListCategory.UPCOMING -> viewModel.upcomingMovies.collectAsStateWithLifecycle()
        MovieListCategory.TOP_RATED -> viewModel.topRatedMovies.collectAsStateWithLifecycle()
        MovieListCategory.POPULAR -> viewModel.popularMovies.collectAsStateWithLifecycle()
    }

    val categoryDisplayName = when (category) {
        MovieListCategory.NOW_PLAYING -> stringResource(id = R.string.now_playing)
        MovieListCategory.UPCOMING -> stringResource(id = R.string.upcoming)
        MovieListCategory.TOP_RATED -> stringResource(id = R.string.top_rated)
        MovieListCategory.POPULAR -> stringResource(id = R.string.popular)
    }

    ItemsScreen(
        content = content,
        categoryDisplayName = categoryDisplayName,
        appendItems = viewModel::appendItems,
        onItemClick = onItemClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ItemsScreen(
    content: ContentUiState,
    categoryDisplayName: String,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = categoryDisplayName,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(
                        id = com.anshtya.movieinfo.core.ui.R.string.back
                    ),
                    modifier = Modifier
                        .padding(start = 2.dp, end = 4.dp)
                        .noRippleClickable { onBackClick() }
                )
            }
        )

        LazyVerticalContentGrid(
            pagingEnabled = true,
            itemsEmpty = content.items.isEmpty(),
            isLoading = content.isLoading,
            endReached = content.endReached,
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            appendItems = { appendItems(content.category) }
        ) {
            items(
                items = content.items,
                key = { it.id }
            ) {
                MediaItemCard(
                    posterPath = it.imagePath,
                    onItemClick = { onItemClick("${it.id},${MediaType.MOVIE}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }
            if (content.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}