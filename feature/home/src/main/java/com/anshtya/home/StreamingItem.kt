package com.anshtya.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anshtya.data.model.StreamingItem

@Composable
fun StreamingItem(
    streamingItem: StreamingItem,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(150.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Column {
            Text(streamingItem.title)
        }
    }
}