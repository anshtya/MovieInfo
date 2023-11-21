package com.anshtya.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.anshtya.data.model.StreamingItem

@Composable
fun StreamingItemCard(
    streamingItem: StreamingItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(140.dp)
    ) {
        Card(
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            StreamingItemImage(imageUrl = streamingItem.posterPath)
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = streamingItem.title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun StreamingItemImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(modifier.height(200.dp)) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500$imageUrl",
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}