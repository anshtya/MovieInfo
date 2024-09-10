package com.anshtya.movieinfo.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.R

const val onboardingNavigationRoute = "onboarding"

@Composable
fun OnboardingScreen(
    navigateToAuth: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        var contentVisible by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            contentVisible = true
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(200.dp))

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn() + slideInHorizontally(
                    animationSpec = tween(easing = LinearOutSlowInEasing),
                    initialOffsetX = { -it }
                )
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(10.dp))

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn() + slideInHorizontally(
                    animationSpec = tween(easing = LinearOutSlowInEasing),
                    initialOffsetX = { -it }
                )
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_text),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp)
        ) {
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn() + slideInVertically(
                    animationSpec = tween(durationMillis = 400),
                    initialOffsetY = { it }
                )
            ) {
                Button(
                    onClick = navigateToAuth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.get_started),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}