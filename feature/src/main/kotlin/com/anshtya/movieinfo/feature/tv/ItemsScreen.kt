package com.anshtya.movieinfo.feature.tv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.content.TvShowListCategory
import com.anshtya.movieinfo.feature.R
import com.anshtya.movieinfo.feature.ui.LazyVerticalContentGrid
import com.anshtya.movieinfo.feature.ui.MediaItemCard
import com.anshtya.movieinfo.feature.ui.TopAppBarWithBackButton

private val horizontalPadding = 8.dp

@Composable
fun ItemsRoute(
    categoryName: String,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TvShowsViewModel
) {
    val category = enumValueOf<TvShowListCategory>(categoryName)
    val content by when (category) {
        TvShowListCategory.AIRING_TODAY -> viewModel.airingTodayTvShows.collectAsStateWithLifecycle()
        TvShowListCategory.POPULAR -> viewModel.popularTvShows.collectAsStateWithLifecycle()
        TvShowListCategory.TOP_RATED -> viewModel.topRatedTvShows.collectAsStateWithLifecycle()
        TvShowListCategory.ON_THE_AIR -> viewModel.onAirTvShows.collectAsStateWithLifecycle()
    }
    val categoryDisplayName = when (category) {
        TvShowListCategory.AIRING_TODAY -> stringResource(id = R.string.airing_today)
        TvShowListCategory.ON_THE_AIR -> stringResource(id = R.string.on_air)
        TvShowListCategory.TOP_RATED -> stringResource(id = R.string.top_rated)
        TvShowListCategory.POPULAR -> stringResource(id = R.string.popular)
    }
    ItemsScreen(
        content = content,
        categoryDisplayName = categoryDisplayName,
        appendItems = viewModel::appendItems,
        onItemClick = { onItemClick("$it,${MediaType.TV}") },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ItemsScreen(
    content: ContentUiState,
    categoryDisplayName: String,
    appendItems: (TvShowListCategory) -> Unit,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarWithBackButton(
                title = {
                    Text(
                        text = categoryDisplayName,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
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
                        onItemClick = { onItemClick("${it.id},${MediaType.TV}") },
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
}