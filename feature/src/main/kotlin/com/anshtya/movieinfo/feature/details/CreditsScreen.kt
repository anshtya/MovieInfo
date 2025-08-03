package com.anshtya.movieinfo.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.data.model.MediaType
import com.anshtya.movieinfo.data.model.details.people.Credits
import com.anshtya.movieinfo.feature.R
import com.anshtya.movieinfo.feature.ui.PersonImage
import com.anshtya.movieinfo.feature.ui.TopAppBarWithBackButton
import com.anshtya.movieinfo.feature.ui.noRippleClickable

@Composable
internal fun CreditsRoute(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel
) {
    val details by viewModel.contentDetailsUiState.collectAsStateWithLifecycle()

    CreditsScreen(
        details = details,
        onItemClick = onItemClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreditsScreen(
    details: ContentDetailUiState,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarWithBackButton(
                title = {
                    Text(
                        text = stringResource(id = R.string.credits),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            when (details) {
                is ContentDetailUiState.Movie -> {
                    CreditsLazyColumn(
                        credits = details.data.credits,
                        onItemClick = onItemClick
                    )
                }

                is ContentDetailUiState.TV -> {
                    CreditsLazyColumn(
                        credits = details.data.credits,
                        onItemClick = onItemClick
                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun CreditsLazyColumn(
    credits: Credits,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
    ) {
        stickyHeader {
            CategoryHeader(stringResource(id = R.string.cast))
        }
        items(
            items = credits.cast,
            key = { it.id }
        ) {
            CreditsItem(
                name = it.name,
                role = it.character,
                imagePath = it.profilePath,
                onItemClick = {
                    onItemClick("${it.id},${MediaType.PERSON}")
                }
            )
        }

        if (credits.crew.isNotEmpty()) {
            item {
                CategoryHeader(text = stringResource(id = R.string.crew))
            }

            val crewListByDepartment = credits.crew.groupBy { it.department }
            crewListByDepartment.forEach { mapEntry ->
                stickyHeader {
                    CategoryHeader(text = mapEntry.key)
                }
                items(
                    items = mapEntry.value,
                    key = { it.creditId }
                ) {
                    CreditsItem(
                        name = it.name,
                        role = it.job,
                        imagePath = it.profilePath,
                        onItemClick = {
                            onItemClick("${it.id},${MediaType.PERSON}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CreditsItem(
    name: String,
    role: String,
    imagePath: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onItemClick() }
            .padding(horizontal = horizontalPadding, vertical = 6.dp)
    ) {
        PersonImage(
            imageUrl = imagePath,
            modifier = Modifier.size(64.dp)
        )

        Spacer(Modifier.width(10.dp))

        Column {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(2.dp))
            Text(text = role)
        }
    }
}

@Composable
private fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp)
    )
}