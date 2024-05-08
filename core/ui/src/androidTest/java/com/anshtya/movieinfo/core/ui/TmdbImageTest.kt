package com.anshtya.movieinfo.core.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test

class TmdbImageTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testContext = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun textDisplayed_whenImageUrlIsEmpty() {
        composeTestRule.setContent {
            TmdbImage(width = 500, imageUrl = "")
        }

        composeTestRule
            .onNodeWithText(
                testContext.resources.getText(R.string.no_image_available).toString()
            )
            .assertIsDisplayed()
    }
}