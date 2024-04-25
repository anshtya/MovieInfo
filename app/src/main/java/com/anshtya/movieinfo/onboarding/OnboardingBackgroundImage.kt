package com.anshtya.movieinfo.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.R
import com.anshtya.movieinfo.ui.theme.md_theme_light_primary
import com.anshtya.movieinfo.ui.theme.md_theme_light_secondary

@Composable
fun OnboardingBackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.onboarding_background),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxSize()
            .blur(
                radiusX = 5.dp,
                radiusY = 5.dp
            )
            .drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(
                        md_theme_light_primary,
                        md_theme_light_secondary
                    ),
                    startY = size.height / 3,
                    endY = size.height
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(
                        brush = gradient,
                        blendMode = BlendMode.Luminosity
                    )
                }
            }
    )
}