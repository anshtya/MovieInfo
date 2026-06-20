package com.anshtya.movieinfo.ui.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Onboarding

fun NavGraphBuilder.onboardingNavigation(
    onOnboardingCompleted: () -> Unit
) {
    composable<Onboarding> {
        OnboardingScreen(
            onOnboardingCompleted = onOnboardingCompleted
        )
    }
}