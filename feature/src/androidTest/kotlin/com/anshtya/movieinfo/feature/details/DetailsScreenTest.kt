package com.anshtya.movieinfo.feature.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.anshtya.movieinfo.data.repository.test.data.testMovieDetail
import com.anshtya.movieinfo.feature.R
import org.junit.Rule
import org.junit.Test

class DetailsScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun circularProgressIndicator_whenLoading_displayed() {
        val loadingContentDescription = composeTestRule.activity
            .getString(R.string.details_loading)

        composeTestRule.setContent {
            DetailsScreen(
                uiState = DetailsUiState(),
                contentDetailsUiState = ContentDetailUiState.Loading,
                onHideBottomSheet = {},
                onErrorShown = {},
                onFavoriteClick = {},
                onWatchlistClick = {},
                onItemClick = {},
                onSeeAllCastClick = {},
                onSignInClick = {},
                onBackClick = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(loadingContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun signInSheet_whenStateIsTrue_exists() {
        val signInSheetContentDescription = composeTestRule.activity
            .getString(R.string.details_sign_in_sheet)

        composeTestRule.setContent {
            DetailsScreen(
                uiState = DetailsUiState(showSignInSheet = true),
                contentDetailsUiState = ContentDetailUiState.Movie(testMovieDetail),
                onHideBottomSheet = {},
                onErrorShown = {},
                onFavoriteClick = {},
                onWatchlistClick = {},
                onItemClick = {},
                onSeeAllCastClick = {},
                onSignInClick = {},
                onBackClick = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(signInSheetContentDescription)
            .assertExists()
    }
}