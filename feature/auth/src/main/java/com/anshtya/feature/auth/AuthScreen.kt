package com.anshtya.feature.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.core.ui.noRippleClickable
import kotlinx.coroutines.launch

@Composable
internal fun AuthRoute(
    hideOnboarding: Boolean?,
    navigateToMovies: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthScreen(
        uiState = uiState,
        hideOnboarding = hideOnboarding,
        onLogIn = {
            hideOnboarding?.let {
                if (it) onBackClick() else navigateToMovies()
            }
        },
        onBackClick = onBackClick,
        onLogInClick = viewModel::logIn,
        onContinueWithoutSignInClick = viewModel::setHideOnboarding,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onErrorShown = viewModel::onErrorShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthScreen(
    uiState: AuthUiState,
    hideOnboarding: Boolean?,
    onLogIn: () -> Unit,
    onBackClick: () -> Unit,
    onLogInClick: () -> Unit,
    onContinueWithoutSignInClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onErrorShown: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        uiState.isLoggedIn?.let { onLogIn() }
    }

    uiState.errorMessage?.let {
        scope.launch { snackbarHostState.showSnackbar(it) }
        onErrorShown()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(
                            id = com.anshtya.core.ui.R.string.back
                        ),
                        modifier = Modifier.noRippleClickable { onBackClick() }
                    )
                }
            )

            Spacer(Modifier.height(100.dp))

            val focusManager = LocalFocusManager.current
            var passwordVisible by remember { mutableStateOf(false) }

            Text(
                text = stringResource(id = R.string.sign_in),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = uiState.username,
                onValueChange = onUsernameChange,
                label = { Text(stringResource(id = R.string.username)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(id = R.string.password)) },
                placeholder = { Text(stringResource(id = R.string.password)) },
                singleLine = true,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        if (passwordVisible) {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = stringResource(id = R.string.hide_password),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = stringResource(id = R.string.show_password),
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(Modifier.height(20.dp))

            if (uiState.isLoading) {
                val authIndicatorDescription = stringResource(
                    id = R.string.auth_circular_progress_indicator
                )
                CircularProgressIndicator(
                    modifier = Modifier.semantics {
                        contentDescription = authIndicatorDescription
                    }
                )
            } else {
                Button(
                    onClick = {
                        onLogInClick()
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .width(250.dp)
                ) {
                    val signInText = stringResource(id = R.string.sign_in)
                    Text(
                        text = signInText,
                        modifier = Modifier.semantics { contentDescription = signInText }
                    )
                }
            }

            hideOnboarding?.let {
                Spacer(Modifier.height(10.dp))

                if (!it) {
                    Button(
                        onClick = onContinueWithoutSignInClick,
                        modifier = Modifier
                            .height(48.dp)
                            .width(250.dp)
                    ) {
                        Text(stringResource(id = R.string.continue_without_sign_in))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    AuthScreen(
        uiState = AuthUiState(),
        hideOnboarding = false,
        onLogIn = {},
        onBackClick = {},
        onLogInClick = {},
        onContinueWithoutSignInClick = {},
        onUsernameChange = {},
        onPasswordChange = {},
        onErrorShown = {}
    )
}