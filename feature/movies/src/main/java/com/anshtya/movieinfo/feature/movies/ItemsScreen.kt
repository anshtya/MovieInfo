package com.anshtya.movieinfo.feature.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.anshtya.core.model.MediaType
import com.anshtya.core.model.content.MovieListCategory
import com.anshtya.core.ui.MediaItemCard
import com.anshtya.core.ui.noRippleClickable

private val horizontalPadding = 10.dp

@Composable
fun ItemsRoute(
    categoryName: String,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: MoviesViewModel
) {
    val category = enumValueOf<MovieListCategory>(categoryName)
    val content = when (category) {
        MovieListCategory.NOW_PLAYING -> viewModel.nowPlayingMovies.collectAsLazyPagingItems()
        MovieListCategory.UPCOMING -> viewModel.upcomingMovies.collectAsLazyPagingItems()
        MovieListCategory.TOP_RATED -> viewModel.topRatedMovies.collectAsLazyPagingItems()
        MovieListCategory.POPULAR -> viewModel.popularMovies.collectAsLazyPagingItems()
    }
    val categoryDisplayName = when (category) {
        MovieListCategory.NOW_PLAYING -> stringResource(id = R.string.now_playing)
        MovieListCategory.UPCOMING -> stringResource(id = R.string.upcoming)
        MovieListCategory.TOP_RATED -> stringResource(id = R.string.top_rated)
        MovieListCategory.POPULAR -> stringResource(id = R.string.popular)
    }
    ItemsScreen(
        content = LazyPagingContent(content),
        categoryDisplayName = categoryDisplayName,
        onItemClick = { onItemClick("$it,${MediaType.MOVIE}") },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ItemsScreen(
    content: LazyPagingContent,
    categoryDisplayName: String,
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
                    contentDescription = stringResource(id = com.anshtya.core.ui.R.string.back),
                    modifier = Modifier
                        .padding(start = 2.dp, end = 4.dp)
                        .noRippleClickable { onBackClick() }
                )
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = content.items.itemCount,
                key = content.items.itemKey { it.id }
            ) { index ->
                content.items[index]?.let { contentItem ->
                    MediaItemCard(
                        posterPath = contentItem.imagePath,
                        onItemClick = { onItemClick("${contentItem.id}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )
                }
            }
        }
    }
}