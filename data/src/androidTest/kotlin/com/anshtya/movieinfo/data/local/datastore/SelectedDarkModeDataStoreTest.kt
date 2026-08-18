package com.anshtya.movieinfo.data.local.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.anshtya.movieinfo.data.model.SelectedDarkMode
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class SelectedDarkModeDataStoreTest {
    private lateinit var userPreferencesDataStore: UserPreferencesDataStore

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setUp() {
        userPreferencesDataStore = UserPreferencesDataStore(
            PreferenceDataStoreFactory.create {
                tmpFolder.newFile("selected_dark_mode_test.preferences_pb")
            }
        )
    }

    @Test
    fun darkModeDefaultIsSystem() = runTest {
        TestCase.assertEquals(
            SelectedDarkMode.SYSTEM,
            userPreferencesDataStore.userData.first().darkMode
        )
    }

    @Test
    fun darkModeRoundTrip_dark() = runTest {
        userPreferencesDataStore.setDarkModePreference(SelectedDarkMode.DARK)

        TestCase.assertEquals(
            SelectedDarkMode.DARK,
            userPreferencesDataStore.userData.first().darkMode
        )
    }

    @Test
    fun darkModeRoundTrip_light() = runTest {
        userPreferencesDataStore.setDarkModePreference(SelectedDarkMode.LIGHT)

        TestCase.assertEquals(
            SelectedDarkMode.LIGHT,
            userPreferencesDataStore.userData.first().darkMode
        )
    }

    @Test
    fun darkModeRoundTrip_backToSystem() = runTest {
        userPreferencesDataStore.setDarkModePreference(SelectedDarkMode.DARK)
        userPreferencesDataStore.setDarkModePreference(SelectedDarkMode.SYSTEM)

        TestCase.assertEquals(
            SelectedDarkMode.SYSTEM,
            userPreferencesDataStore.userData.first().darkMode
        )
    }
}
