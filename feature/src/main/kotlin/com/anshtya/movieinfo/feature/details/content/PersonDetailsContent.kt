package com.anshtya.movieinfo.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.data.model.details.people.PersonDetails
import com.anshtya.movieinfo.feature.R
import com.anshtya.movieinfo.feature.details.OverviewSection
import com.anshtya.movieinfo.feature.ui.MediaItemCard
import com.anshtya.movieinfo.feature.ui.TopAppBarWithBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PersonDetailsContent(
    personDetails: PersonDetails,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pinnedScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBarWithBackButton(
                title = {
                    Text(
                        text = personDetails.name,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onBackClick = onBackClick
            )
        },
        modifier = Modifier.nestedScroll(pinnedScrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                Row(Modifier.fillMaxWidth()) {
                    MediaItemCard(
                        personDetails.profilePath,
                        modifier = Modifier.size(height = 200.dp, width = 140.dp)
                    )

                    Spacer(Modifier.width(10.dp))

                    PersonInfoSection(
                        name = personDetails.name,
                        gender = personDetails.gender,
                        birthday = personDetails.birthday,
                        deathday = personDetails.deathday,
                        department = personDetails.knownForDepartment
                    )
                }
            }

            item {
                PersonDetailsSection(
                    alsoKnownAs = personDetails.alsoKnownAs,
                    placeOfBirth = personDetails.placeOfBirth
                )
            }

            item { OverviewSection(personDetails.biography) }
        }
    }
}

@Composable
private fun PersonInfoSection(
    name: String,
    gender: String,
    birthday: String,
    deathday: String?,
    department: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        DetailItem(
            fieldName = stringResource(id = R.string.gender),
            value = gender
        )

        DetailItem(
            fieldName = stringResource(id = R.string.born),
            value = birthday
        )

        deathday?.let {
            DetailItem(
                fieldName = stringResource(id = R.string.died),
                value = it
            )
        }

        DetailItem(
            fieldName = stringResource(id = R.string.known_for),
            value = department
        )
    }
}

@Composable
private fun PersonDetailsSection(
    alsoKnownAs: String,
    placeOfBirth: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DetailItem(
            fieldName = stringResource(id = R.string.birth_place),
            value = placeOfBirth
        )
        DetailItem(
            fieldName = stringResource(id = R.string.also_known_as),
            value = alsoKnownAs
        )
    }
}