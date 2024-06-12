package com.anshtya.movieinfo.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun PersonImage(
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
            TmdbImage(
                width = 300,
                imageUrl = imageUrl
            )
        }
    }
}

@Composable
fun TmdbImage(
    width: Int,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize()) {
        if (imageUrl.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_image_available),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            SubcomposeAsyncImage(
                model = "https://image.tmdb.org/t/p/w${width}${imageUrl}",
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                loading = {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(20.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}