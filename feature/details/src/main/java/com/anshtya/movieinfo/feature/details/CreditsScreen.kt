package com.anshtya.movieinfo.feature.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.core.model.MediaType
import com.anshtya.movieinfo.core.model.details.people.Credits
import com.anshtya.movieinfo.core.ui.UserImage
import com.anshtya.movieinfo.core.ui.noRippleClickable

private val horizontalPadding = 8.dp

@Composable
internal fun CreditsRoute(
    onItemClick: (String) -> Unit,
    viewModel: DetailsViewModel
) {
    val details by viewModel.contentDetailsUiState.collectAsStateWithLifecycle()

    CreditsScreen(
        details = details,
        onItemClick = onItemClick
    )
}

@Composable
private fun CreditsScreen(
    details: ContentDetailUiState,
    onItemClick: (String) -> Unit
) {
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

@OptIn(ExperimentalFoundationApi::class)
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
            .padding(horizontal = horizontalPadding,vertical = 6.dp)
    ) {
        CreditImage(
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

@Composable
private fun CreditImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    UserImage(
        imageUrl = imageUrl,
        modifier = modifier
    )
}