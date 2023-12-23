package com.anshtya.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.anshtya.data.model.FreeItem
import com.anshtya.data.model.PopularItem
import com.anshtya.data.model.TrendingItem
import com.anshtya.ui.StreamingItemCard

private val horizontalPadding = 10.dp

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val timeWindowOptions = homeViewModel.timeWindowOptions
    val popularContentFilters = homeViewModel.popularContentFilters
    val freeContentTypes = homeViewModel.freeContentTypes
    val trendingMovies = homeViewModel.trendingMovies.collectAsLazyPagingItems()
    val popularContent = homeViewModel.popularContent.collectAsLazyPagingItems()
    val freeContent = homeViewModel.freeContent.collectAsLazyPagingItems()
    val selectedTimeWindow by homeViewModel.selectedTimeWindowIndex.collectAsStateWithLifecycle()
    val selectedContentFilter by homeViewModel.selectedContentFilterIndex.collectAsStateWithLifecycle()
    val selectedFreeContent by homeViewModel.selectedFreeContentIndex.collectAsStateWithLifecycle()

    HomeScreen(
        trendingMovies = trendingMovies,
        popularContent = popularContent,
        freeContent = freeContent,
        timeWindowOptions = timeWindowOptions,
        popularContentFilters = popularContentFilters,
        freeContentTypes = freeContentTypes,
        selectedTimeWindow = selectedTimeWindow,
        selectedContentFilter = selectedContentFilter,
        selectedFreeContent = selectedFreeContent,
        onTimeWindowClick = homeViewModel::setTrendingTimeWindow,
        onFilterClick = homeViewModel::setPopularContentFilter,
        onFreeContentClick = homeViewModel::setFreeContentType
    )
}

@Composable
fun HomeScreen(
    trendingMovies: LazyPagingItems<TrendingItem>,
    popularContent: LazyPagingItems<PopularItem>,
    freeContent: LazyPagingItems<FreeItem>,
    timeWindowOptions: List<Int>,
    popularContentFilters: List<Int>,
    freeContentTypes: List<Int>,
    selectedTimeWindow: Int,
    selectedContentFilter: Int,
    selectedFreeContent: Int,
    onTimeWindowClick: (Int) -> Unit,
    onFilterClick: (Int) -> Unit,
    onFreeContentClick: (Int) -> Unit
) {
    Surface {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            trendingSection(
                trendingMovies = trendingMovies,
                timeWindowOptions = timeWindowOptions,
                selectedTimeWindow = selectedTimeWindow,
                onTimeWindowClick = onTimeWindowClick
            )
            popularContentSection(
                popularContent = popularContent,
                popularContentFilters = popularContentFilters,
                selectedContentFilter = selectedContentFilter,
                onFilterClick = onFilterClick
            )
            freeToWatchSection(
                freeContent = freeContent,
                freeContentTypes = freeContentTypes,
                selectedContentType = selectedFreeContent,
                onTypeClick = onFreeContentClick
            )
        }
    }
}

private fun LazyListScope.trendingSection(
    trendingMovies: LazyPagingItems<TrendingItem>,
    timeWindowOptions: List<Int>,
    selectedTimeWindow: Int,
    onTimeWindowClick: (Int) -> Unit
) {
    item {
        val lazyRowState = rememberLazyListState()
        LaunchedEffect(selectedTimeWindow) {
            lazyRowState.scrollToItem(0)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContentSection(
                sectionName = R.string.trending,
                options = timeWindowOptions,
                selectedOptionIndex = selectedTimeWindow,
                onOptionClick = onTimeWindowClick
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (trendingMovies.loadState.mediator?.refresh is LoadState.Loading) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = horizontalPadding),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = lazyRowState,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            count = trendingMovies.itemCount
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
}

private fun LazyListScope.popularContentSection(
    popularContent: LazyPagingItems<PopularItem>,
    popularContentFilters: List<Int>,
    selectedContentFilter: Int,
    onFilterClick: (Int) -> Unit
) {
    item {
        val lazyRowState = rememberLazyListState()
        LaunchedEffect(selectedContentFilter) {
            lazyRowState.scrollToItem(0)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContentSection(
                sectionName = R.string.popular,
                options = popularContentFilters,
                selectedOptionIndex = selectedContentFilter,
                onOptionClick = onFilterClick
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val isLoading by remember(popularContent.loadState.source) {
                    derivedStateOf { popularContent.loadState.source.refresh is LoadState.Loading }
                }
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
                            count = popularContent.itemCount
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
}

private fun LazyListScope.freeToWatchSection(
    freeContent: LazyPagingItems<FreeItem>,
    freeContentTypes: List<Int>,
    selectedContentType: Int,
    onTypeClick: (Int) -> Unit
) {
    item {
        val lazyRowState = rememberLazyListState()
        LaunchedEffect(selectedContentType) {
            lazyRowState.scrollToItem(0)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContentSection(
                sectionName = R.string.free_to_watch,
                options = freeContentTypes,
                selectedOptionIndex = selectedContentType,
                onOptionClick = onTypeClick
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val isLoading by remember(freeContent.loadState.source) {
                    derivedStateOf { freeContent.loadState.source.refresh is LoadState.Loading }
                }
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
                            count = freeContent.itemCount
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
}

@Composable
fun ContentSection(
    @StringRes sectionName: Int,
    options: List<Int>,
    selectedOptionIndex: Int,
    onOptionClick: (Int) -> Unit
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
        OptionsDropdownMenu(
            options = options,
            selectedOptionIndex = selectedOptionIndex,
            onOptionClick = onOptionClick
        )
    }
}

@Composable
fun OptionsDropdownMenu(
    options: List<Int>,
    selectedOptionIndex: Int,
    onOptionClick: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedOption = options[selectedOptionIndex]
    Box {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .size(height = 28.dp, width = 100.dp)
                .clickable { isExpanded = !isExpanded }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = selectedOption))
            }
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = !isExpanded }
        ) {
            options.forEachIndexed { index, option ->
                if (index != selectedOptionIndex) {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = option))
                        },
                        onClick = {
                            onOptionClick(index)
                            isExpanded = !isExpanded
                        }
                    )
                }
            }
        }
    }
}