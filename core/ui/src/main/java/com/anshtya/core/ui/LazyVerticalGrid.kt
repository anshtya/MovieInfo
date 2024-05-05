package com.anshtya.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LazyVerticalContentGrid(
    isLoading: Boolean = false,
    endReached: Boolean = false,
    itemsEmpty: Boolean = false,
    appendItems: () -> Unit = {},
    pagingEnabled: Boolean,
    contentPadding: PaddingValues,
    content: LazyGridScope.() -> Unit
) {
    val lazyGridState = rememberLazyGridState()

    if (pagingEnabled) {
        val isAtBottom by remember {
            derivedStateOf {
                val layoutInfo = lazyGridState.layoutInfo
                if (layoutInfo.totalItemsCount == 0) {
                    false
                } else {
                    val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
                    val viewPortHeight =
                        layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset

                    val isLastItemReached = lastVisibleItem.index + 1 == layoutInfo.totalItemsCount
                    val isLastItemDisplayed =
                        lastVisibleItem.offset.y + lastVisibleItem.size.height <= viewPortHeight

                    (isLastItemReached && isLastItemDisplayed)
                }
            }
        }

        val shouldAppend = isAtBottom && !isLoading && !endReached
        LaunchedEffect(isAtBottom) {
            if (shouldAppend) appendItems()
        }
    }

    Box(Modifier.fillMaxWidth()) {
        if (itemsEmpty && isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = contentPadding,
                state = lazyGridState,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
                content = content
            )
        }
    }
}