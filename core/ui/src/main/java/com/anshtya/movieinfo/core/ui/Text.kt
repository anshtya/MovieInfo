package com.anshtya.movieinfo.core.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult

@Composable
fun AnnotatedClickableText(
    attributionString: AnnotatedString,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val pressIndicator = Modifier.pointerInput(onClick) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layoutResult ->
                onClick(layoutResult.getOffsetForPosition(pos))
            }
        }
    }

    Text(
        text = attributionString,
        onTextLayout = { layoutResult.value = it },
        modifier = modifier.then(pressIndicator)
    )
}