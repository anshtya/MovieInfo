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
import androidx.compose.runtime.getValue
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
import com.anshtya.core.model.FreeItem
import com.anshtya.core.model.PopularItem
import com.anshtya.core.model.TrendingItem
import com.anshtya.core.ui.FilterDropdownMenu
import com.anshtya.core.ui.StreamingItemCard

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
        trendingMovies = trendingMovies,
        popularContent = popularContent,
        freeContent = freeContent,
        trendingContentFilters = trendingContentFilters,
        popularContentFilters = popularContentFilters,
        freeContentFilters = freeContentFilters,
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
    trendingMovies: LazyPagingItems<TrendingItem>,
    popularContent: LazyPagingItems<PopularItem>,
    freeContent: LazyPagingItems<FreeItem>,
    trendingContentFilters: List<Int>,
    popularContentFilters: List<Int>,
    freeContentFilters: List<Int>,
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
                trendingMovies = trendingMovies,
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
    trendingMovies: LazyPagingItems<TrendingItem>,
    filters: List<Int>,
    selectedFilterIndex: Int,
    onFilterClick: (Int) -> Unit
) {
    item {
        val lazyRowState = rememberLazyListState()
        LaunchedEffect(selectedFilterIndex) {
            lazyRowState.scrollToItem(0)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContentSection(
                sectionName = R.string.trending,
                filters = filters,
                selectedFilterIndex = selectedFilterIndex,
                onFilterClick = onFilterClick
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    state = lazyRowState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = trendingMovies.itemCount,
                        key = trendingMovies.itemKey { it.id }
                    ) { index ->
                        trendingMovies[index]?.let { streamingItem ->
                            StreamingItemCard(
                                posterPath = streamingItem.imagePath,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                    item {
                        if (trendingMovies.loadState.mediator?.append is LoadState.Loading) {
                            Box(Modifier.fillMaxHeight()) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.popularContentSection(
    popularContent: LazyPagingItems<PopularItem>,
    filters: List<Int>,
    selectedFilterIndex: Int,
    onFilterClick: (Int) -> Unit
) {
    item {
        val lazyRowState = rememberLazyListState()
        LaunchedEffect(selectedFilterIndex) {
            lazyRowState.scrollToItem(0)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContentSection(
                sectionName = R.string.popular,
                filters = filters,
                selectedFilterIndex = selectedFilterIndex,
                onFilterClick = onFilterClick
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    state = lazyRowState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = popularContent.itemCount,
                        key = popularContent.itemKey { it.id }
                    ) { index ->
                        popularContent[index]?.let { streamingItem ->
                            StreamingItemCard(
                                posterPath = streamingItem.imagePath,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                    item {
                        if (popularContent.loadState.mediator?.append is LoadState.Loading) {
                            Box(Modifier.fillMaxHeight()) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.freeToWatchSection(
    freeContent: LazyPagingItems<FreeItem>,
    filters: List<Int>,
    selectedFilterIndex: Int,
    onFilterClick: (Int) -> Unit
) {
    item {
        val lazyRowState = rememberLazyListState()
        LaunchedEffect(selectedFilterIndex) {
            lazyRowState.scrollToItem(0)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContentSection(
                sectionName = R.string.free_to_watch,
                filters = filters,
                selectedFilterIndex = selectedFilterIndex,
                onFilterClick = onFilterClick
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    state = lazyRowState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = freeContent.itemCount,
                        key = freeContent.itemKey { it.id }
                    ) { index ->
                        freeContent[index]?.let { streamingItem ->
                            StreamingItemCard(
                                posterPath = streamingItem.imagePath,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                    item {
                        if (freeContent.loadState.mediator?.append is LoadState.Loading) {
                            Box(Modifier.fillMaxHeight()) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentSection(
    @StringRes sectionName: Int,
    filters: List<Int>,
    selectedFilterIndex: Int,
    onFilterClick: (Int) -> Unit
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
}