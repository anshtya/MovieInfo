package com.anshtya.feature.search

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.anshtya.core.model.SearchItem
import com.anshtya.core.model.SearchSuggestion
import com.anshtya.core.ui.ErrorView
import com.anshtya.core.ui.MovieInfoTopAppBar
import com.anshtya.core.ui.SearchResultCard

@Composable
internal fun SearchRoute(
    onSearchResultClick: (Int) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchInput by viewModel.searchInput.collectAsStateWithLifecycle()
    val searchSuggestions by viewModel.searchSuggestions.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val searchResultTabs = viewModel.searchResultTabs
    val selectedResultTab by viewModel.selectedResultTabIndex.collectAsStateWithLifecycle()
    val showError by viewModel.showError.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    SearchScreen(
        searchResults = searchResults,
        searchResultTabs = searchResultTabs,
        selectedResultTab = selectedResultTab,
        searchInput = searchInput,
        searchQuery = searchQuery,
        isSearching = isSearching,
        showError = showError,
        searchSuggestions = searchSuggestions,
        onSearchQueryChange = viewModel::changeSearchQuery,
        onSearch = viewModel::onSearch,
        onBack = viewModel::onBack,
        onChangeSearchResultTab = viewModel::changeSearchResultTab,
        onSearchResultClick = onSearchResultClick,
        onErrorShown = viewModel::onErrorShown
    )
}

@Composable
internal fun SearchScreen(
    searchResults: LazyPagingItems<SearchItem>,
    searchResultTabs: List<Int>,
    selectedResultTab: Int,
    searchQuery: String,
    searchInput: String,
    isSearching: Boolean,
    showError: Boolean?,
    searchSuggestions: List<SearchSuggestion>,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    onChangeSearchResultTab: (Int) -> Unit,
    onSearchResultClick: (Int) -> Unit,
    onErrorShown: () -> Unit
) {
    var active by remember { mutableStateOf(false) }
    val context = LocalContext.current

    showError?.let {
        Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show()
        onErrorShown()
    }

    val pagingError by remember(searchResults.loadState.source.append) {
        derivedStateOf { searchResults.loadState.source.append is LoadState.Error }
    }
    if (pagingError) {
        Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    Column(Modifier.fillMaxSize()) {
        MovieInfoTopAppBar(
            query = if (active) searchQuery else searchInput,
            onQueryChange = {
                if (active) {
                    onSearchQueryChange(it)
                }
            },
            onSearch = onSearch,
            onBackClick = {
                if (active) {
                    active = false
                } else {
                    onBack()
                }
            },
            active = active,
            onActiveChange = { active = it }
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
                    searchResultTabs = searchResultTabs,
                    selectedResultTabIndex = selectedResultTab,
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
private fun SearchHistoryContent(
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
private fun SearchResultContent(
    searchResults: LazyPagingItems<SearchItem>,
    searchResultTabs: List<Int>,
    selectedResultTabIndex: Int,
    isSearching: Boolean,
    active: Boolean,
    onBack: () -> Unit,
    onChangeSearchResultTab: (Int) -> Unit,
    onSearchResultClick: (Int) -> Unit
) {
    BackHandler(isSearching && !active) { onBack() }

    val isLoading = searchResults.loadState.source.refresh is LoadState.Loading
    val isError = searchResults.loadState.source.refresh is LoadState.Error
    Column {
        TabRow(
            selectedTabIndex = selectedResultTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            searchResultTabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedResultTabIndex == index,
                    onClick = { onChangeSearchResultTab(index) },
                    text = { Text(stringResource(id = tab)) }
                )
            }
        }
        Box(Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else if (isError) {
                ErrorView(
                    onRetryButtonClick = { searchResults.retry() },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = searchResults.itemCount,
                        key = searchResults.itemKey { it.id }
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