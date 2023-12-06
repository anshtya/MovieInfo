package com.anshtya.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun StreamingItemCard(
    posterPath: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier.width(140.dp)
    ) {
        StreamingItemImage(
            imageUrl = posterPath
        )
    }
}

@Composable
fun StreamingItemImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500$imageUrl",
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}