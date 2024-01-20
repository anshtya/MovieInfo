package com.anshtya.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteButton(
    active: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(52.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = stringResource(id = R.string.favorite),
                tint = if (active) {
                    Color.Red
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {}
            )
        }
    }
}

@Composable
fun WatchlistButton(
    active: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(52.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Rounded.Bookmark,
                contentDescription = stringResource(id = R.string.watchlist),
                tint = if (active) {
                    Color.Red
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {}
            )
        }
    }
}