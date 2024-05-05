package com.anshtya.feature.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun toastAppears_whenErrorState() {
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
}