package com.anshtya.movieinfo.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Locale

private val starColor = Color(0xFFF2D349)

@Composable
fun Rating(
    rating: Double,
    count: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = starColor
            )
            Text(
                text = "${String.format(Locale.getDefault(),"%.1f", rating)}/5"
            )
        }
        Text(
            text = "($count)",
            color = Color.Gray
        )
    }
}