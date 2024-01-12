package com.anshtya.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun TmdbImage(
    imageUrl: String
) {
    Box {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500$imageUrl",
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun BackdropImage(
    imageUrl: String
) {
    Box {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w1280$imageUrl",
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun UserImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.LightGray,
        shape = CircleShape,
        modifier = modifier
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w300$imageUrl",
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}