package com.anshtya.feature.you

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.core.model.SelectedDarkMode
import com.anshtya.core.model.SelectedDarkMode.DARK
import com.anshtya.core.model.SelectedDarkMode.LIGHT
import com.anshtya.core.model.SelectedDarkMode.SYSTEM
import com.anshtya.feature.you.SettingsUiState.Loading
import com.anshtya.feature.you.SettingsUiState.Success

@Composable
internal fun YouRoute(
    viewModel: YouViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    YouScreen(
        settingsUiState = settingsUiState,
        onChangeTheme = viewModel::setDynamicColorPreference,
        onChangeDarkMode = viewModel::setDarkModePreference,
        onChangeIncludeAdult = viewModel::setAdultResultPreference
    )
}

@Composable
internal fun YouScreen(
    settingsUiState: SettingsUiState,
    onChangeTheme: (Boolean) -> Unit,
    onChangeDarkMode: (SelectedDarkMode) -> Unit,
    onChangeIncludeAdult: (Boolean) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        val isLoggedIn by remember { mutableStateOf(false) }
        var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

        if (showSettingsDialog) {
            SettingsDialog(
                settingsUiState = settingsUiState,
                onChangeTheme = onChangeTheme,
                onChangeDarkMode = onChangeDarkMode,
                onChangeIncludeAdult = onChangeIncludeAdult,
                onDismissRequest = { showSettingsDialog = !showSettingsDialog }
            )
        }

        when (getScreenType(isLoggedIn)) {
            ScreenType.LOGGED_IN -> {}
            ScreenType.LOGGED_OUT -> {}
        }
        IconButton(
            onClick = { showSettingsDialog = true },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(id = R.string.settings_dialog_title)
            )
        }
    }
}


@Composable
fun SettingsDialog(
    settingsUiState: SettingsUiState,
    onChangeTheme: (Boolean) -> Unit,
    onChangeDarkMode: (SelectedDarkMode) -> Unit,
    onChangeIncludeAdult: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.settings_dialog_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Divider()
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                when (settingsUiState) {
                    Loading -> {
                        CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                    }

                    is Success -> {
                        SettingsPanel(
                            settings = settingsUiState.userSettings,
                            onChangeTheme = onChangeTheme,
                            onChangeDarkMode = onChangeDarkMode,
                            onChangeIncludeAdult = onChangeIncludeAdult
                        )
                    }
                }
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.settings_dialog_dismiss_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismissRequest() },
            )
        },
    )
}

@Composable
private fun SettingsPanel(
    settings: UserSettings,
    onChangeTheme: (Boolean) -> Unit,
    onChangeDarkMode: (SelectedDarkMode) -> Unit,
    onChangeIncludeAdult: (Boolean) -> Unit,
) {
    if (supportsDynamicColorTheme()) {
        SettingsDialogSectionTitle(text = stringResource(id = R.string.settings_dialog_theme))
        Column(Modifier.selectableGroup()) {
            SettingsDialogChooserRow(
                text = stringResource(id = R.string.settings_dialog_theme_default),
                selected = !settings.useDynamicColor,
                onClick = { onChangeTheme(false) }
            )
            SettingsDialogChooserRow(
                text = stringResource(id = R.string.settings_dialog_theme_dynamic),
                selected = settings.useDynamicColor,
                onClick = { onChangeTheme(true) }
            )
        }
    }
    SettingsDialogSectionTitle(text = stringResource(id = R.string.settings_dialog_dark_mode))
    Column(Modifier.selectableGroup()) {
        SettingsDialogChooserRow(
            text = stringResource(id = R.string.settings_dialog_dark_default),
            selected = settings.darkMode == SYSTEM,
            onClick = { onChangeDarkMode(SYSTEM) }
        )
        SettingsDialogChooserRow(
            text = stringResource(id = R.string.settings_dialog_dark_yes),
            selected = settings.darkMode == DARK,
            onClick = { onChangeDarkMode(DARK) }
        )
        SettingsDialogChooserRow(
            text = stringResource(id = R.string.settings_dialog_dark_no),
            selected = settings.darkMode == LIGHT,
            onClick = { onChangeDarkMode(LIGHT) }
        )
    }
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SettingsDialogSectionTitle(text = stringResource(id = R.string.settings_dialog_adult))
        Switch(
            checked = settings.includeAdultResults,
            onCheckedChange = onChangeIncludeAdult
        )
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun SettingsDialogChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Text(text)
    }
}

@Composable
private fun getScreenType(
    isLoggedIn: Boolean
): ScreenType {
    return if (isLoggedIn) {
        ScreenType.LOGGED_IN
    } else {
        ScreenType.LOGGED_OUT
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicColorTheme() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

private enum class ScreenType {
    LOGGED_IN, LOGGED_OUT
}

@Preview(showBackground = true)
@Composable
private fun SettingsDialogPreview() {
    SettingsDialog(
        settingsUiState = Success(
            UserSettings(
                useDynamicColor = true,
                includeAdultResults = true,
                darkMode = SYSTEM
            )
        ),
        onChangeTheme = {},
        onChangeDarkMode = {},
        onChangeIncludeAdult = {},
        onDismissRequest = {}
    )
}