package com.anshtya.core.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieInfoSearchBar(
    value: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    BasicTextField(
        value = textFieldValue,
        onValueChange = { newTextFieldValueState ->
            textFieldValueState = newTextFieldValueState
            onQueryChange(newTextFieldValueState.text)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            if (value.isNotEmpty()) {
                onSearch()
                onActiveChange(false)
            }
        }),
        maxLines = 1,
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { if (it.isFocused) onActiveChange(true) }
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = textFieldValue.text,
            innerTextField = innerTextField,
            enabled = enabled,
            singleLine = true,
            colors = colors,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            placeholder = { if (textFieldValue.text.isEmpty()) Text(stringResource(id = R.string.search)) },
            trailingIcon = {
                if (textFieldValue.text.isNotEmpty() && active) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.clear)
                        )
                    }
                }
            },
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                top = 0.dp, bottom = 0.dp
            )
        )
    }

    LaunchedEffect(active) {
        if (!active) focusManager.clearFocus()
    }

    BackHandler(enabled = active) {
        onActiveChange(false)
    }
}

@Composable
@Preview
fun MovieInfoSearchBarPreview() {
    MovieInfoSearchBar(
        value = "query",
        onQueryChange = {},
        onSearch = {},
        active = true,
        onActiveChange = {}
    )
}