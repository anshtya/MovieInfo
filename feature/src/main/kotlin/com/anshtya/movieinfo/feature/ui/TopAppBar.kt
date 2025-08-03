package com.anshtya.movieinfo.feature.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.anshtya.movieinfo.feature.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBackButton(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    iconButtonColors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = title,
        navigationIcon = {
            IconButton(
                colors = iconButtonColors,
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        },
        colors = topAppBarColors,
        modifier = modifier
    )
}