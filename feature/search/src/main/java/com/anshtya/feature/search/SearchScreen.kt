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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.core.model.SearchItem
import com.anshtya.core.ui.MovieInfoTopSearchAppBar

@Composable
internal fun SearchRoute(
    onSearchResultClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchSuggestions by viewModel.searchSuggestions.collectAsStateWithLifecycle()
    val showError by viewModel.showError.collectAsStateWithLifecycle()

    SearchScreen(
        searchQuery = searchQuery,
        showError = showError,
        searchSuggestions = searchSuggestions,
        onSearchQueryChange = viewModel::changeSearchQuery,
        onBack = viewModel::onBack,
        onSearchResultClick = onSearchResultClick,
        onErrorShown = viewModel::onErrorShown
    )
}

@Composable
internal fun SearchScreen(
    searchQuery: String,
    showError: Boolean?,
    searchSuggestions: List<SearchItem>,
    onSearchQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onSearchResultClick: (String) -> Unit,
    onErrorShown: () -> Unit
) {
    var showSearchSuggestions by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    showError?.let {
        Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show()
        onErrorShown()
    }

    Column(Modifier.fillMaxSize()) {
        MovieInfoTopSearchAppBar(
            query = searchQuery,
            onQueryChange = { onSearchQueryChange(it) },
            onSearch = {},
            onBackClick = {
                showSearchSuggestions = false
                onBack()
            },
            active = showSearchSuggestions,
            onActiveChange = { showSearchSuggestions = it }
        )
        if (showSearchSuggestions) {
            BackHandler {
                showSearchSuggestions = false
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        SearchHistoryContent(history = listOf())
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