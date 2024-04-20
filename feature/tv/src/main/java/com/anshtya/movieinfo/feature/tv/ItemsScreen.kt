package com.anshtya.movieinfo.feature.tv

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
import com.anshtya.core.model.content.TvShowListCategory
import com.anshtya.core.ui.MediaItemCard
import com.anshtya.core.ui.noRippleClickable

private val horizontalPadding = 10.dp

@Composable
fun ItemsRoute(
    categoryName: String,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TvShowsViewModel
) {
    val category = enumValueOf<TvShowListCategory>(categoryName)
    val content = when (category) {
        TvShowListCategory.AIRING_TODAY -> viewModel.airingTodayTvShows.collectAsLazyPagingItems()
        TvShowListCategory.POPULAR -> viewModel.popularTvShows.collectAsLazyPagingItems()
        TvShowListCategory.TOP_RATED -> viewModel.topRatedTvShows.collectAsLazyPagingItems()
        TvShowListCategory.ON_THE_AIR -> viewModel.onAirTvShows.collectAsLazyPagingItems()
    }
    val categoryDisplayName = when (category) {
        TvShowListCategory.AIRING_TODAY -> stringResource(id = R.string.airing_today)
        TvShowListCategory.ON_THE_AIR -> stringResource(id = R.string.on_air)
        TvShowListCategory.TOP_RATED -> stringResource(id = R.string.top_rated)
        TvShowListCategory.POPULAR -> stringResource(id = R.string.popular)
    }
    ItemsScreen(
        content = LazyPagingContent(content),
        categoryDisplayName = categoryDisplayName,
        onItemClick = { onItemClick("$it,${MediaType.TV}") },
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