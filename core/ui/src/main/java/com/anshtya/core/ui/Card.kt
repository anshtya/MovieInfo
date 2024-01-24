package com.anshtya.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MediaItemCard(
    posterPath: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier.clickable { onItemClick() }
    ) {
        Box(Modifier.fillMaxSize()) {
            if (posterPath.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_image_available),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                TmdbImage(imageUrl = posterPath)
            }
        }
    }
}