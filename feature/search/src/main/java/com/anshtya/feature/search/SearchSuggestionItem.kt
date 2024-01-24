package com.anshtya.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anshtya.core.ui.MediaItemCard

@Composable
internal fun SearchSuggestionItem(
    name: String,
    imagePath: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Column(Modifier.clickable { onItemClick() }) {
            MediaItemCard(
                posterPath = imagePath,
                onItemClick = onItemClick,
                modifier = Modifier.height(200.dp)
            )
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}