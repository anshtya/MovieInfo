package com.anshtya.movieinfo.feature.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.core.model.SearchItem
import com.anshtya.movieinfo.core.ui.MovieInfoSearchBar
import kotlinx.coroutines.launch

@Composable
internal fun SearchRoute(
    navigateToDetail: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchSuggestions by viewModel.searchSuggestions.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    SearchScreen(
        searchQuery = searchQuery,
        errorMessage = errorMessage,
        searchSuggestions = searchSuggestions,
        onSearchQueryChange = viewModel::changeSearchQuery,
        onBack = viewModel::onBack,
        onSearchResultClick = navigateToDetail,
        onErrorShown = viewModel::onErrorShown
    )
}

@Composable
internal fun SearchScreen(
    searchQuery: String,
    errorMessage: String?,
    searchSuggestions: List<SearchItem>,
    onSearchQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onSearchResultClick: (String) -> Unit,
    onErrorShown: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    errorMessage?.let {
        scope.launch { snackbarHostState.showSnackbar(it) }
        onErrorShown()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MovieInfoSearchBar(
                value = searchQuery,
                onQueryChange = { onSearchQueryChange(it) }
            )

            if (searchQuery.isNotEmpty()) {
                BackHandler {
                    onSearchQueryChange("")
                    onBack()
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(140.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = searchSuggestions,
                        key = { it.id }
                    ) {
                        SearchSuggestionItem(
                            name = it.name,
                            imagePath = it.imagePath,
                            onItemClick = {
                                // Converting type to uppercase for [MediaType]
                                onSearchResultClick("${it.id},${it.mediaType.uppercase()}")
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            SearchHistoryContent(history = listOf())
        }
    }
}

@Composable
private fun SearchHistoryContent(
    history: List<String>
) {
    Box(Modifier.fillMaxSize()) {
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