package com.anshtya.feature.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.anshtya.core.model.MediaItem
import com.anshtya.core.ui.FilterDropdownMenu
import com.anshtya.core.ui.MediaItemCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private val horizontalPadding = 10.dp

@Composable
internal fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val trendingContentFilters = homeViewModel.trendingContentFilters
    val popularContentFilters = homeViewModel.popularContentFilters
    val freeContentFilters = homeViewModel.freeContentFilters
    val trendingMovies = homeViewModel.trendingMovies.collectAsLazyPagingItems()
    val popularContent = homeViewModel.popularContent.collectAsLazyPagingItems()
    val freeContent = homeViewModel.freeContent.collectAsLazyPagingItems()
    val selectedTrendingContentFilterIndex by homeViewModel.selectedTrendingContentFilterIndex.collectAsStateWithLifecycle()
    val selectedPopularContentFilterIndex by homeViewModel.selectedPopularContentFilterIndex.collectAsStateWithLifecycle()
    val selectedFreeContentFilterIndex by homeViewModel.selectedFreeContentFilterIndex.collectAsStateWithLifecycle()

    HomeScreen(
        trendingMovies = LazyPagingContent(trendingMovies),
        popularContent = LazyPagingContent(popularContent),
        freeContent = LazyPagingContent(freeContent),
        trendingContentFilters = trendingContentFilters.toImmutableList(),
        popularContentFilters = popularContentFilters.toImmutableList(),
        freeContentFilters = freeContentFilters.toImmutableList(),
        selectedTrendingContentFilterIndex = selectedTrendingContentFilterIndex,
        selectedPopularContentFilterIndex = selectedPopularContentFilterIndex,
        selectedFreeContentFilterIndex = selectedFreeContentFilterIndex,
        onTrendingContentFilterClick = homeViewModel::setTrendingContentFilter,
        onPopularContentFilterClick = homeViewModel::setPopularContentFilter,
        onFreeContentFilterClick = homeViewModel::setFreeContentFilter
    )
}

@Composable
internal fun HomeScreen(
    trendingMovies: LazyPagingContent,
    popularContent: LazyPagingContent,
    freeContent: LazyPagingContent,
    trendingContentFilters: ImmutableList<Int>,
    popularContentFilters: ImmutableList<Int>,
    freeContentFilters: ImmutableList<Int>,
    selectedTrendingContentFilterIndex: Int,
    selectedPopularContentFilterIndex: Int,
    selectedFreeContentFilterIndex: Int,
    onTrendingContentFilterClick: (Int) -> Unit,
    onPopularContentFilterClick: (Int) -> Unit,
    onFreeContentFilterClick: (Int) -> Unit
) {
    Surface {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            trendingSection(
                trendingContent = trendingMovies,
                filters = trendingContentFilters,
                selectedFilterIndex = selectedTrendingContentFilterIndex,
                onFilterClick = onTrendingContentFilterClick
            )
            popularContentSection(
                popularContent = popularContent,
                filters = popularContentFilters,
                selectedFilterIndex = selectedPopularContentFilterIndex,
                onFilterClick = onPopularContentFilterClick
            )
            freeToWatchSection(
                freeContent = freeContent,
                filters = freeContentFilters,
                selectedFilterIndex = selectedFreeContentFilterIndex,
                onFilterClick = onFreeContentFilterClick
            )
        }
    }
}

private fun LazyListScope.trendingSection(
    trendingContent: LazyPagingContent,
    filters: ImmutableList<Int>,
    selectedFilterIndex: Int,
    onFilterClick: (Int) -> Unit
) {
    item {
        ContentSection(
            content = trendingContent,
            sectionName = R.string.trending,
            filters = filters,
            selectedFilterIndex = selectedFilterIndex,
            onFilterClick = onFilterClick
        )
    }
}

private fun LazyListScope.popularContentSection(
    popularContent: LazyPagingContent,
    filters: ImmutableList<Int>,
    selectedFilterIndex: Int,
    onFilterClick: (Int) -> Unit
) {
    item {
        ContentSection(
            content = popularContent,
            sectionName = R.string.popular,
            filters = filters,
            selectedFilterIndex = selectedFilterIndex,
            onFilterClick = onFilterClick
        )
    }
}

private fun LazyListScope.freeToWatchSection(
    freeContent: LazyPagingContent,
    filters: ImmutableList<Int>,
    selectedFilterIndex: Int,
    onFilterClick: (Int) -> Unit
) {
    item {
        ContentSection(
            content = freeContent,
            sectionName = R.string.free_to_watch,
            filters = filters,
            selectedFilterIndex = selectedFilterIndex,
            onFilterClick = onFilterClick
        )
    }
}

@Composable
private fun ContentSection(
    content: LazyPagingContent,
    @StringRes sectionName: Int,
    filters: ImmutableList<Int>,
    selectedFilterIndex: Int,
    onFilterClick: (Int) -> Unit
) {

    val isLoading by remember(content.items.loadState.refresh) {
        derivedStateOf { content.items.loadState.refresh is LoadState.Loading }
    }

    val lazyRowState = rememberLazyListState()
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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = sectionName),
                style = MaterialTheme.typography.headlineMedium
            )
            FilterDropdownMenu(
                filters = filters,
                selectedFilterIndex = selectedFilterIndex,
                onFilterClick = onFilterClick
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
                        content.items[index]?.let { streamingItem ->
                            MediaItemCard(
                                posterPath = streamingItem.imagePath,
                                modifier = Modifier.fillMaxHeight()
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
    val items: LazyPagingItems<MediaItem>
)