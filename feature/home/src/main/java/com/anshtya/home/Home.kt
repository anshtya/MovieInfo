package com.anshtya.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.anshtya.data.model.StreamingItem
import com.anshtya.ui.StreamingItemCard

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val trendingMovies = homeViewModel.trendingMovies.collectAsLazyPagingItems()
    val savedTimeWindow by homeViewModel.savedTimeWindow.collectAsStateWithLifecycle()

    Home(
        trendingMovies = trendingMovies,
        savedTimeWindow = savedTimeWindow,
        onItemClick = { homeViewModel.setTrendingTimeWindow(it) }
    )
}

@Composable
fun Home(
    trendingMovies: LazyPagingItems<StreamingItem>,
    savedTimeWindow: TrendingTimeWindow,
    onItemClick: (TrendingTimeWindow) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                val lazyRowState = rememberLazyListState()
                LaunchedEffect(savedTimeWindow) {
                    lazyRowState.scrollToItem(0)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = stringResource(id = R.string.trending),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    MyDropdownMenu(
                        listItems = TrendingTimeWindow.values().toList(),
                        savedTimeWindow = savedTimeWindow,
                        onItemClick = onItemClick
                    )
                }
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    state = lazyRowState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        count = trendingMovies.itemCount
                    ) { index ->
                        trendingMovies[index]?.let { streamingItem ->
                            StreamingItemCard(streamingItem = streamingItem)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyDropdownMenu(
    listItems: List<TrendingTimeWindow>,
    savedTimeWindow: TrendingTimeWindow,
    onItemClick: (TrendingTimeWindow) -> Unit
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
                Text(text = stringResource(id = savedTimeWindow.uiLabel))
            }
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            listItems.forEach {
                if (it != savedTimeWindow) {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = it.uiLabel))
                        },
                        onClick = {
                            onItemClick(it)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}