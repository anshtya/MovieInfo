package com.anshtya.movieinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.movieinfo.MainActivityUiState.Loading
import com.anshtya.movieinfo.MainActivityUiState.Success
import com.anshtya.movieinfo.ui.MovieInfoApp
import com.anshtya.movieinfo.ui.theme.MovieInfoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val splashScreen = installSplashScreen()

        var uiState: MainActivityUiState by mutableStateOf(Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect {}
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                Loading -> true
                is Success -> false
            }
        }

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)
            val dynamicColor = shouldUseDynamicColor(uiState)

            MovieInfoTheme(
                darkTheme = darkTheme,
                dynamicColor = dynamicColor
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.hideOnboarding?.let {
                        MovieInfoApp(hideOnboarding = it)
                    }
                }
            }
        }
    }
}

@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState
): Boolean {
    return when (uiState) {
        Loading -> isSystemInDarkTheme()
        is Success -> when (uiState.darkMode) {
            SelectedDarkMode.SYSTEM -> isSystemInDarkTheme()
            SelectedDarkMode.DARK -> true
            SelectedDarkMode.LIGHT -> false
        }
    }
}

@Composable
private fun shouldUseDynamicColor(
    uiState: MainActivityUiState
): Boolean {
    return when (uiState) {
        Loading -> false
        is Success -> uiState.useDynamicColor
    }
}