package com.anshtya.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage

@Composable
internal fun TmdbImage(
    width: Int,
    imageUrl: String
) {
    Box(Modifier.fillMaxSize()) {
        if (imageUrl.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_image_available),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w${width}${imageUrl}",
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
fun BackdropImage(
    imageUrl: String
) {
    TmdbImage(
        width = 1280,
        imageUrl = imageUrl
    )
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
        if (imageUrl.isEmpty()) {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w300$imageUrl",
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}