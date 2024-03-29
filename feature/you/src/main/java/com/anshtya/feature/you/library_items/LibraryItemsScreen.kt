package com.anshtya.feature.you.library_items

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.core.model.library.LibraryItem
import com.anshtya.core.ui.ErrorText
import com.anshtya.core.ui.MediaItemCard
import com.anshtya.feature.you.R
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
internal fun LibraryItemsRoute(
    onBackClick: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
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
        onNavigateToDetails = onNavigateToDetails,
        onErrorShown = viewModel::onErrorShown
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun LibraryItemsScreen(
    movieItems: List<LibraryItem>,
    tvItems: List<LibraryItem>,
    libraryItemType: LibraryItemType?,
    errorMessage: ErrorText?,
    onDeleteItem: (LibraryItem, LibraryItemType) -> Unit,
    onMediaTypeChange: (LibraryMediaType) -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onBackClick: () -> Unit,
    onErrorShown: () -> Unit
) {
    val context = LocalContext.current
    errorMessage?.let {
        Toast.makeText(context, it.toText(context), Toast.LENGTH_SHORT).show()
        onErrorShown()
    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                libraryItemType?.let {
                    Text(text = stringResource(id = it.displayName))
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = com.anshtya.core.ui.R.string.back)
                    )
                }
            }
        )

        val libraryMediaTabs = LibraryMediaType.entries
        val pagerState = rememberPagerState(pageCount = { libraryMediaTabs.size })
        val scope = rememberCoroutineScope()
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
                            libraryItemType = libraryItemType,
                            onLibraryItemClick = onNavigateToDetails,
                            onDeleteClick = onDeleteItem
                        )
                    }

                    LibraryMediaType.TV -> {
                        LibraryContent(
                            content = tvItems,
                            libraryItemType = libraryItemType,
                            onLibraryItemClick = onNavigateToDetails,
                            onDeleteClick = onDeleteItem
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryContent(
    content: List<LibraryItem>,
    libraryItemType: LibraryItemType?,
    onLibraryItemClick: (String) -> Unit,
    onDeleteClick: (LibraryItem, LibraryItemType) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        if (content.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_items),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(140.dp),
                contentPadding = PaddingValues(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    items = content,
                    key = { it.id }
                ) {
                    LibraryItem(
                        imagePath = it.imagePath,
                        onClick = { onLibraryItemClick("${it.id},${it.mediaType}") },
                        onDeleteClick = { onDeleteClick(it, libraryItemType!!) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryItem(
    imagePath: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        MediaItemCard(
            posterPath = imagePath,
            onItemClick = onClick
        )

        Surface(
            shape = CircleShape,
            color = Color.LightGray.copy(alpha = 0.4f),
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