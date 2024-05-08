package com.anshtya.feature.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailsScreenTest {
    private lateinit var loadingContentDescription: String
    private lateinit var signInSheetContentDescription: String

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        with(composeTestRule.activity) {
            loadingContentDescription = getString(R.string.details_loading)
            signInSheetContentDescription = getString(R.string.details_sign_in_sheet)
        }
    }

    @Test
    fun circularProgressIndicator_whenLoading_displayed() {
        composeTestRule.setContent {
            DetailsScreen(
                uiState = DetailsUiState(isLoading = true),
                contentDetailsUiState = ContentDetailUiState.Empty,
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
        composeTestRule.setContent {
            DetailsScreen(
                uiState = DetailsUiState(showSignInSheet = true),
                contentDetailsUiState = ContentDetailUiState.Empty,
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