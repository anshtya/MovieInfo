package com.anshtya.movieinfo.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.R

@Composable
fun OnboardingScreen(
    navigateToAuth: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Box(Modifier.fillMaxSize()) {
        val animationState = remember {
            MutableTransitionState(false).apply {
                targetState = true
            }
        }

        OnboardingBackgroundImage()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(200.dp))

            AnimatedVisibility(
                visibleState = animationState,
                enter = fadeIn() + slideInHorizontally(
                    animationSpec = tween(easing = LinearOutSlowInEasing),
                    initialOffsetX = { -it }
                )
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(10.dp))

            AnimatedVisibility(
                visibleState = animationState,
                enter = fadeIn() + slideInHorizontally(
                    animationSpec = tween(easing = LinearOutSlowInEasing),
                    initialOffsetX = { -it }
                )
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_text),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .align(Alignment.BottomCenter)
        ) {
            AnimatedVisibility(
                visibleState = animationState,
                enter = fadeIn() + slideInVertically(
                    animationSpec = tween(durationMillis = 400),
                    initialOffsetY = { it }
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val attributionString = buildAnnotatedString {
                        append("Photo by ")

                        pushStringAnnotation(
                            tag = "photo",
                            annotation = "https://unsplash.com/photos/the-walking-dead-dvd-movie-wMkaMXTJjlQ"
                        )
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Samuel Regan-Asante")
                        }

                        append(" on ")

                        pushStringAnnotation(
                            tag = "unsplash",
                            annotation = "https://unsplash.com"
                        )
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Unsplash")
                        }
                    }
                    AttributionText(
                        attributionString = attributionString,
                        onClick = { offset ->
                            attributionString.getStringAnnotations(
                                tag = "photo",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let { uriHandler.openUri(it.item) }

                            attributionString.getStringAnnotations(
                                tag = "unsplash",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let { uriHandler.openUri(it.item) }
                        }
                    )

                    Button(
                        onClick = navigateToAuth,
                        colors = ButtonDefaults.buttonColors(contentColor = Color.Black),
                        modifier = Modifier
                            .height(48.dp)
                            .width(250.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.get_started),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.disclaimer),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(
        navigateToAuth = {}
    )
}