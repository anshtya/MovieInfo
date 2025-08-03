package com.anshtya.movieinfo.feature.ui

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MediaItemCard(
    posterPath: String,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
            .size(width = 120.dp, height = 160.dp)
            .noRippleClickable { onItemClick() }
    ) {
        TmdbImage(
            width = 500,
            imageUrl = posterPath
        )
    }
}