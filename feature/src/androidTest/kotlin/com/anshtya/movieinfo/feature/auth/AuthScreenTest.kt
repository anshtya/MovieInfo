package com.anshtya.movieinfo.feature.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.anshtya.movieinfo.feature.R
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun snackBar_whenError_appears() {
        val errorMessage = "error message"
        composeTestRule.setContent {
            AuthScreen(
                uiState = AuthUiState(
                    errorMessage = errorMessage
                ),
                hideOnboarding = null,
                onLogInClick = {},
                onErrorShown = {},
                onUsernameChange = {},
                onPasswordChange = {},
                onBackClick = {},
                onContinueWithoutSignInClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun circularProgressIndicator_whenLoading_appears() {
        val authIndicatorDescription = composeTestRule.activity
            .getString(R.string.auth_circular_progress_indicator)

        val signInText = composeTestRule.activity
            .getString(R.string.sign_in)

        composeTestRule.setContent {
            AuthScreen(
                uiState = AuthUiState(
                    isLoading = true
                ),
                hideOnboarding = null,
                onLogInClick = {},
                onErrorShown = {},
                onUsernameChange = {},
                onPasswordChange = {},
                onBackClick = {},
                onContinueWithoutSignInClick = {}
            )
        }

        composeTestRule
            .onNode(hasContentDescription(authIndicatorDescription))
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasContentDescription(signInText))
            .assertIsNotDisplayed()
    }

    @Test
    fun continueWithoutSignIn_whenPreviouslyOnboarded_hidden() {
        val continueText = composeTestRule.activity
            .getString(R.string.continue_without_sign_in)

        composeTestRule.setContent {
            AuthScreen(
                uiState = AuthUiState(),
                hideOnboarding = null,
                onLogInClick = {},
                onErrorShown = {},
                onUsernameChange = {},
                onPasswordChange = {},
                onBackClick = {},
                onContinueWithoutSignInClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(continueText)
            .assertDoesNotExist()
    }

    @Test
    fun continueWithoutSignIn_whenNotOnboarded_shown() {
        val continueText = composeTestRule.activity
            .getString(R.string.continue_without_sign_in)

        composeTestRule.setContent {
            AuthScreen(
                uiState = AuthUiState(),
                hideOnboarding = false,
                onLogInClick = {},
                onErrorShown = {},
                onUsernameChange = {},
                onPasswordChange = {},
                onBackClick = {},
                onContinueWithoutSignInClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(continueText)
            .assertIsDisplayed()
    }
}