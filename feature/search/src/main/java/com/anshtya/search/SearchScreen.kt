package com.anshtya.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.data.model.SearchSuggestion
import com.anshtya.ui.MovieInfoTopAppBar

@Composable
fun SearchRoute(
    onSearch: (String) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchSuggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()

    SearchScreen(
        searchQuery = searchQuery,
        searchSuggestions = searchSuggestions,
        onSearchQueryChange = searchViewModel::changeSearchQuery,
        onSearch = onSearch,
        onBackClick = onBackClick
    )
}

@Composable
fun SearchScreen(
    searchQuery: String,
    searchSuggestions: List<SearchSuggestion>,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var active by remember { mutableStateOf(false) }
    Column {
        MovieInfoTopAppBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onSearchClick = onSearch,
            onBackClick = onBackClick,
            active = active,
            onActiveChange = {
                active = it
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = searchSuggestions.take(6),
                    key = { it.id }
                ) {
                    SearchSuggestionItem(name = it.name, imagePath = it.imagePath)
                }
            }
        }
        LazyColumn {

        }
    }
}