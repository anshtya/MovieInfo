package com.anshtya.feature.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.anshtya.core.ui.ErrorText
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun snackbar_whenErrorOccurs_exists() {
        composeTestRule.setContent {
            AuthScreen(
                uiState = AuthUiState(
                    errorMessage = ErrorText.StringResource(id = R.string.error_message)
                ),
                onLogIn = {},
                onLogInClick = {},
                onErrorShown = {},
                onUsernameChange = {},
                onPasswordChange = {}
            )
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.error_message))
            .assertIsDisplayed()
    }
}