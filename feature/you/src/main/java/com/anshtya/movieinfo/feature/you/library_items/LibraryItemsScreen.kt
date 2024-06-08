package com.anshtya.movieinfo.feature.you.library_items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.core.ui.MediaItemCard
import com.anshtya.movieinfo.core.ui.LazyVerticalContentGrid
import com.anshtya.movieinfo.core.model.library.LibraryItem
import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.feature.you.R
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
internal fun LibraryItemsRoute(
    onBackClick: () -> Unit,
    navigateToDetails: (String) -> Unit,
    viewModel: LibraryItemsViewModel = hiltViewModel()
) {
    val movieItems by viewModel.movieItems.collectAsStateWithLifecycle()
    val tvItems by viewModel.tvItems.collectAsStateWithLifecycle()
    val libraryItemType by viewModel.libraryItemType.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    LibraryItemsScreen(
        movieItems = movieItems,
        tvItems = tvItems,
        libraryItemType = libraryItemType,
        errorMessage = errorMessage,
        onDeleteItem = viewModel::deleteItem,
        onMediaTypeChange = viewModel::onMediaTypeChange,
        onBackClick = onBackClick,
        onItemClick = navigateToDetails,
        onErrorShown = viewModel::onErrorShown
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun LibraryItemsScreen(
    movieItems: List<LibraryItem>,
    tvItems: List<LibraryItem>,
    libraryItemType: LibraryItemType?,
    errorMessage: String?,
    onDeleteItem: (LibraryItem) -> Unit,
    onMediaTypeChange: (LibraryMediaType) -> Unit,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onErrorShown: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    errorMessage?.let {
        scope.launch { snackbarHostState.showSnackbar(it) }
        onErrorShown()
    }

    val libraryItemTitle = when (libraryItemType) {
        LibraryItemType.FAVORITE -> stringResource(id = R.string.favorites)
        LibraryItemType.WATCHLIST -> stringResource(id = R.string.watchlist)
        else -> null
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TopAppBar(
                title = {
                    libraryItemTitle?.let { Text(text = it) }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                id = com.anshtya.movieinfo.core.ui.R.string.back
                            )
                        )
                    }
                }
            )

            val libraryMediaTabs = LibraryMediaType.entries
            val pagerState = rememberPagerState(pageCount = { libraryMediaTabs.size })

            val selectedTabIndex by remember(pagerState.currentPage) {
                mutableIntStateOf(pagerState.currentPage)
            }

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }
                    .distinctUntilChanged()
                    .collect { onMediaTypeChange(libraryMediaTabs[it]) }
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                libraryMediaTabs.forEachIndexed { index, mediaTypeTab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = stringResource(id = mediaTypeTab.displayName)) }
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Column(Modifier.fillMaxSize()) {
                    when (libraryMediaTabs[page]) {
                        LibraryMediaType.MOVIE -> {
                            LibraryContent(
                                content = movieItems,
                                onItemClick = onItemClick,
                                onDeleteClick = onDeleteItem
                            )
                        }

                        LibraryMediaType.TV -> {
                            LibraryContent(
                                content = tvItems,
                                onItemClick = onItemClick,
                                onDeleteClick = onDeleteItem
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryContent(
    content: List<LibraryItem>,
    onItemClick: (String) -> Unit,
    onDeleteClick: (LibraryItem) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        if (content.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_items),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalContentGrid(
                pagingEnabled = false,
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                items(
                    items = content,
                    key = { it.id }
                ) {
                    LibraryItem(
                        posterPath = it.imagePath,
                        onItemClick = {
                            onItemClick("${it.id},${it.mediaType.uppercase()}")
                        },
                        onDeleteClick = { onDeleteClick(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryItem(
    posterPath: String,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box {
        MediaItemCard(
            posterPath = posterPath,
            onItemClick = onItemClick
        )

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(42.dp)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete),
                tint = Color.Black,
                modifier = Modifier
                    .clickable(onClick = onDeleteClick)
                    .padding(4.dp)
            )
        }
    }
}