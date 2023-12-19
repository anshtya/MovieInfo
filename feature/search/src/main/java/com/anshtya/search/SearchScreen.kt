package com.anshtya.search

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.anshtya.data.model.SearchItem
import com.anshtya.data.model.SearchSuggestion
import com.anshtya.ui.MovieInfoTopAppBar
import com.anshtya.ui.SearchResultCard

@Composable
fun SearchRoute(
    onSearchResultClick: (Int) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchInput by viewModel.searchInput.collectAsStateWithLifecycle()
    val searchSuggestions by viewModel.searchSuggestions.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val selectedResultTab by viewModel.selectedResultTab.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    SearchScreen(
        searchResults = searchResults,
        selectedResultTab = selectedResultTab,
        searchInput = searchInput,
        searchQuery = searchQuery,
        isSearching = isSearching,
        searchSuggestions = searchSuggestions,
        onSearchQueryChange = viewModel::changeSearchQuery,
        onSearch = viewModel::onSearch,
        onBack = viewModel::onBack,
        onChangeSearchResultTab = viewModel::changeSearchResultTab,
        onSearchResultClick = onSearchResultClick
    )
}

@Composable
fun SearchScreen(
    searchResults: LazyPagingItems<SearchItem>,
    selectedResultTab: SearchResultTab,
    searchQuery: String,
    searchInput: String,
    isSearching: Boolean,
    searchSuggestions: List<SearchSuggestion>,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    onChangeSearchResultTab: (SearchResultTab) -> Unit,
    onSearchResultClick: (Int) -> Unit
) {
    var active by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        MovieInfoTopAppBar(
            query = if (active) searchQuery else searchInput,
            onQueryChange = onSearchQueryChange,
            onSearch = onSearch,
            onBackClick = {
                if (active) {
                    active = false
                } else {
                    onBack()
                }
            },
            active = active,
            onActiveChange = {
                active = it
            }
        )
        if (active) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = searchSuggestions,
                    key = { it.id }
                ) {
                    SearchSuggestionItem(name = it.name, imagePath = it.imagePath)
                }
            }
        }
        when (getScreenType(isSearching)) {
            ScreenType.SearchHistory -> {
                SearchHistoryContent(history = listOf())
            }

            ScreenType.SearchResult -> {
                SearchResultContent(
                    searchResults = searchResults,
                    selectedResultTab = selectedResultTab,
                    isSearching = isSearching,
                    active = active,
                    onBack = onBack,
                    onChangeSearchResultTab = onChangeSearchResultTab,
                    onSearchResultClick = onSearchResultClick
                )
            }
        }
    }
}

@Composable
fun SearchHistoryContent(
    history: List<String>
) {
    Box(Modifier.fillMaxSize()) {
        if (history.isEmpty()) {
            Text(
                text = stringResource(id = R.string.search_text),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = history
                ) {
                    Text(it)
                }
            }
        }
    }
}

@Composable
fun SearchResultContent(
    searchResults: LazyPagingItems<SearchItem>,
    selectedResultTab: SearchResultTab,
    isSearching: Boolean,
    active: Boolean,
    onBack: () -> Unit,
    onChangeSearchResultTab: (SearchResultTab) -> Unit,
    onSearchResultClick: (Int) -> Unit
) {
    BackHandler(isSearching && !active) { onBack() }

    val isLoading by remember(searchResults.loadState.source) {
        derivedStateOf { searchResults.loadState.source.refresh is LoadState.Loading }
    }
    val tabs = SearchResultTab.entries
    val selectedIndex by remember(selectedResultTab) {
        mutableIntStateOf(tabs.indexOf(selectedResultTab))
    }
    Column {
        TabRow(
            selectedTabIndex = selectedIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { onChangeSearchResultTab(tab) },
                    text = { Text(stringResource(id = tab.displayName)) }
                )
            }
        }
        Box(Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = searchResults.itemCount
                    ) { index ->
                        searchResults[index]?.let { searchItem ->
                            SearchResultCard(
                                name = searchItem.name,
                                overview = searchItem.overview,
                                posterPath = searchItem.posterPath,
                                onResultClick = { onSearchResultClick(searchItem.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getScreenType(
    isSearching: Boolean
): ScreenType {
    return if (isSearching) {
        ScreenType.SearchResult
    } else {
        ScreenType.SearchHistory
    }
}

private enum class ScreenType {
    SearchHistory, SearchResult
}

enum class SearchResultTab(@StringRes val displayName: Int) {
    MOVIES(R.string.movies), TV(R.string.tv)
}