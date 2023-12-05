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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
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
import com.anshtya.data.model.StreamingItem
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

    Home(
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
        onFreeContentClick = homeViewModel::setFreeContentType,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    trendingMovies: LazyPagingItems<StreamingItem>,
    popularContent: LazyPagingItems<StreamingItem>,
    freeContent: LazyPagingItems<StreamingItem>,
    @StringRes timeWindowOptions: List<Int>,
    @StringRes popularContentFilters: List<Int>,
    @StringRes freeContentTypes: List<Int>,
    selectedTimeWindow: Int,
    selectedContentFilter: Int,
    selectedFreeContent: Int,
    onTimeWindowClick: (Int) -> Unit,
    onFilterClick: (Int) -> Unit,
    onFreeContentClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    Surface {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                query = text,
                onQueryChange = { text = it },
                active = active,
                onActiveChange = { active = it },
                onSearch = { active = false },
                placeholder = {
                    Text(stringResource(id = R.string.search))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(
                            onClick = { text = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.clear_search)
                            )
                        }
                    }
                }
            ) {
                // TODO: implement search
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 8.dp),
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
}

fun LazyListScope.trendingSection(
    trendingMovies: LazyPagingItems<StreamingItem>,
    @StringRes timeWindowOptions: List<Int>,
    selectedTimeWindow: Int,
    onTimeWindowClick: (Int) -> Unit
) {
    item {
        ContentSectionWithDropdownMenu(
            sectionName = R.string.trending,
            contentList = trendingMovies,
            options = timeWindowOptions,
            selectedOptionIndex = selectedTimeWindow,
            onOptionClick = onTimeWindowClick
        )
    }
}

fun LazyListScope.popularContentSection(
    popularContent: LazyPagingItems<StreamingItem>,
    @StringRes popularContentFilters: List<Int>,
    selectedContentFilter: Int,
    onFilterClick: (Int) -> Unit
) {
    item {
        ContentSectionWithDropdownMenu(
            sectionName = R.string.popular,
            contentList = popularContent,
            options = popularContentFilters,
            selectedOptionIndex = selectedContentFilter,
            onOptionClick = onFilterClick
        )
    }
}

fun LazyListScope.freeToWatchSection(
    freeContent: LazyPagingItems<StreamingItem>,
    @StringRes freeContentTypes: List<Int>,
    selectedContentType: Int,
    onTypeClick: (Int) -> Unit
) {
    item {
        ContentSectionWithDropdownMenu(
            sectionName = R.string.free_to_watch,
            contentList = freeContent,
            options = freeContentTypes,
            selectedOptionIndex = selectedContentType,
            onOptionClick = onTypeClick
        )
    }
}

@Composable
fun ContentSectionWithDropdownMenu(
    @StringRes sectionName: Int,
    contentList: LazyPagingItems<StreamingItem>,
    @StringRes options: List<Int>,
    selectedOptionIndex: Int,
    onOptionClick: (Int) -> Unit
) {
    val selectedOptionName = options[selectedOptionIndex]
    val lazyRowState = rememberLazyListState()
    LaunchedEffect(selectedOptionIndex) {
        lazyRowState.scrollToItem(0)
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
            OptionsDropdownMenu(
                options = options,
                selectedOptionIndex = selectedOptionIndex,
                selectedOption = selectedOptionName,
                onOptionClick = onOptionClick
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val isLoading by remember(contentList.loadState.source) {
                derivedStateOf { contentList.loadState.source.refresh is LoadState.Loading }
            }
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    state = lazyRowState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = contentList.itemCount
                    ) { index ->
                        contentList[index]?.let { streamingItem ->
                            StreamingItemCard(
                                streamingItem = streamingItem,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionsDropdownMenu(
    options: List<Int>,
    @StringRes selectedOption: Int,
    selectedOptionIndex: Int,
    onOptionClick: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Box {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primary,
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
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEachIndexed { index, option ->
                if (index != selectedOptionIndex) {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = option))
                        },
                        onClick = {
                            onOptionClick(index)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}