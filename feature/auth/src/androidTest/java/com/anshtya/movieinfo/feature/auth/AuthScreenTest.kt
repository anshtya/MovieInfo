package com.anshtya.movieinfo.feature.auth

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testContext = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun snackBar_whenError_appears() {
        val errorMessage = "error message"
        composeTestRule.setContent {
            AuthScreen(
                uiState = AuthUiState(
                    errorMessage = errorMessage
                ),
                hideOnboarding = null,
                onLogIn = {},
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
        val authIndicatorDescription = testContext.resources.getText(
            R.string.auth_circular_progress_indicator
        ).toString()

        val signInText = testContext.resources.getText(R.string.sign_in).toString()

        composeTestRule.setContent {
            AuthScreen(
                uiState = AuthUiState(
                    isLoading = true
                ),
                hideOnboarding = null,
                onLogIn = {},
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
        val continueText = testContext.resources.getText(
            R.string.continue_without_sign_in
        ).toString()

        composeTestRule.setContent {
            AuthScreen(
                uiState = AuthUiState(),
                hideOnboarding = null,
                onLogIn = {},
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
        val continueText = testContext.resources.getText(
            R.string.continue_without_sign_in
        ).toString()

        composeTestRule.setContent {
            AuthScreen(
                uiState = AuthUiState(),
                hideOnboarding = false,
                onLogIn = {},
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