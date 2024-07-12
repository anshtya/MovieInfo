package com.anshtya.movieinfo.feature.you

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.anshtya.movieinfo.core.model.SelectedDarkMode
import com.anshtya.movieinfo.core.model.SelectedDarkMode.DARK
import com.anshtya.movieinfo.core.model.SelectedDarkMode.LIGHT
import com.anshtya.movieinfo.core.model.SelectedDarkMode.SYSTEM
import com.anshtya.movieinfo.core.model.library.LibraryItemType
import com.anshtya.movieinfo.core.model.user.AccountDetails
import com.anshtya.movieinfo.core.ui.PersonImage
import kotlinx.coroutines.launch

@Composable
internal fun YouRoute(
    navigateToAuth: () -> Unit,
    navigateToLibraryItem: (String) -> Unit,
    viewModel: YouViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    YouScreen(
        uiState = uiState,
        isLoggedIn = isLoggedIn,
        userSettings = userSettings,
        onChangeTheme = viewModel::setDynamicColorPreference,
        onChangeDarkMode = viewModel::setDarkModePreference,
        onChangeIncludeAdult = viewModel::setAdultResultPreference,
        onNavigateToAuth = navigateToAuth,
        onLibraryItemClick = navigateToLibraryItem,
        onReloadAccountDetailsClick = viewModel::getAccountDetails,
        onRefresh = viewModel::onRefresh,
        onLogOutClick = viewModel::logOut,
        onErrorShown = viewModel::onErrorShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun YouScreen(
    uiState: YouUiState,
    isLoggedIn: Boolean?,
    userSettings: UserSettings?,
    onChangeTheme: (Boolean) -> Unit,
    onChangeDarkMode: (SelectedDarkMode) -> Unit,
    onChangeIncludeAdult: (Boolean) -> Unit,
    onNavigateToAuth: () -> Unit,
    onLibraryItemClick: (String) -> Unit,
    onReloadAccountDetailsClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onRefresh: () -> Unit,
    onErrorShown: () -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState(enabled = { isLoggedIn == true })

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.errorMessage?.let {
        scope.launch { snackbarHostState.showSnackbar(it) }
        onErrorShown()
    }

    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    if (showSettingsDialog) {
        SettingsDialog(
            userSettings = userSettings,
            onChangeTheme = onChangeTheme,
            onChangeDarkMode = onChangeDarkMode,
            onChangeIncludeAdult = onChangeIncludeAdult,
            onDismissRequest = { showSettingsDialog = !showSettingsDialog }
        )
    }

    var showAttributionInfoDialog by rememberSaveable { mutableStateOf(false) }
    if (showAttributionInfoDialog) {
        AttributionInfoDialog(
            onDismissRequest = { showAttributionInfoDialog = !showAttributionInfoDialog }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = { showAttributionInfoDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = stringResource(id = R.string.attribution_info)
                        )
                    }

                    userSettings?.let {
                        IconButton(
                            onClick = { showSettingsDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = stringResource(id = R.string.settings_dialog_title)
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            Column(Modifier.fillMaxSize()) {
                isLoggedIn?.let {
                    if (isLoggedIn) {
                        uiState.accountDetails?.let {
                            LoggedInView(
                                accountDetails = it,
                                isLoggingOut = uiState.isLoggingOut,
                                onLibraryItemClick = onLibraryItemClick,
                                onLogOutClick = onLogOutClick
                            )
                        } ?: LoadAccountDetails(
                            isLoading = uiState.isLoading,
                            onReloadAccountDetailsClick = onReloadAccountDetailsClick
                        )
                    } else {
                        LoggedOutView(
                            onNavigateToAuth = onNavigateToAuth
                        )
                    }
                }
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )

            if (pullToRefreshState.isRefreshing) {
                LaunchedEffect(true) { onRefresh() }
            }

            LaunchedEffect(uiState.isRefreshing) {
                if (uiState.isRefreshing) {
                    pullToRefreshState.startRefresh()
                } else {
                    pullToRefreshState.endRefresh()
                }
            }
        }
    }
}

@Composable
private fun LoggedInView(
    accountDetails: AccountDetails,
    isLoggingOut: Boolean,
    onLibraryItemClick: (String) -> Unit,
    onLogOutClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        PersonImage(
            imageUrl = accountDetails.avatar ?: "",
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = accountDetails.username,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = accountDetails.name,
            style = MaterialTheme.typography.titleMedium,
        )
        LibrarySection(onLibraryItemClick = onLibraryItemClick)

        if (isLoggingOut) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = onLogOutClick,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(stringResource(id = R.string.log_out))
            }
        }
    }
}

@Composable
private fun LoggedOutView(
    onNavigateToAuth: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = stringResource(id = R.string.log_in_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = onNavigateToAuth,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.log_in))
            }
        }
    }
}

@Composable
private fun LoadAccountDetails(
    isLoading: Boolean,
    onReloadAccountDetailsClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = onReloadAccountDetailsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.reload_account_details))
            }
        }
    }
}

@Composable
private fun LibrarySection(
    onLibraryItemClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.your_library),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        LibraryItemOption(
            optionName = stringResource(id = R.string.favorites),
            onClick = { onLibraryItemClick(LibraryItemType.FAVORITE.name) }
        )
        LibraryItemOption(
            optionName = stringResource(id = R.string.watchlist),
            onClick = { onLibraryItemClick(LibraryItemType.WATCHLIST.name) }
        )
    }
}

@Composable
private fun LibraryItemOption(
    optionName: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(42.dp)
    ) {
        Text(
            text = optionName,
            fontSize = 18.sp,
        )
    }
}

@Composable
private fun AttributionInfoDialog(
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
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
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .decoderFactory(SvgDecoder.Factory())
                        .data(R.drawable.tmdb_logo)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.attribution_text),
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
private fun SettingsDialog(
    userSettings: UserSettings?,
    onChangeTheme: (Boolean) -> Unit,
    onChangeDarkMode: (SelectedDarkMode) -> Unit,
    onChangeIncludeAdult: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
) {
    userSettings?.let {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = stringResource(R.string.settings_dialog_title),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            text = {
                HorizontalDivider()
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    SettingsPanel(
                        settings = userSettings,
                        onChangeTheme = onChangeTheme,
                        onChangeDarkMode = onChangeDarkMode,
                        onChangeIncludeAdult = onChangeIncludeAdult
                    )
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

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicColorTheme() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Preview(showBackground = true)
@Composable
private fun SettingsDialogPreview() {
    SettingsDialog(
        userSettings = UserSettings(
            useDynamicColor = true,
            includeAdultResults = true,
            darkMode = SYSTEM
        ),
        onChangeTheme = {},
        onChangeDarkMode = {},
        onChangeIncludeAdult = {}
    ) {}
}