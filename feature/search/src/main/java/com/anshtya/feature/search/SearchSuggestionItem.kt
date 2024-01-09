package com.anshtya.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anshtya.core.ui.MediaItemImage

@Composable
internal fun SearchSuggestionItem(
    name: String,
    imagePath: String,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(10.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.height(200.dp)
        ) {
            if (imagePath.isEmpty()) {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(id = R.string.no_image_available),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                MediaItemImage(
                    imageUrl = imagePath,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(5.dp)
        )
    }
}