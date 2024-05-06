package com.anshtya.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.floor

private val starColor = Color(0xFFF2D349)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Rating(
    rating: Double,
    count: Int
) {
    val filledStars = rating.toInt()
    val isHalfFilledStar = !(floor(rating).minus(rating).equals(0.0))
    val emptyStars = (5 - rating).toInt()

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier.weight(1f, fill = false)
        ) {
            FlowRow {
                repeat(filledStars) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = starColor
                    )
                }
                if (isHalfFilledStar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.StarHalf,
                        contentDescription = null,
                        tint = starColor
                    )
                }
                repeat(emptyStars) {
                    Icon(
                        imageVector = Icons.Rounded.StarBorder,
                        contentDescription = null,
                        tint = starColor
                    )
                }
            }
        }
        Text("($count)")
    }
}