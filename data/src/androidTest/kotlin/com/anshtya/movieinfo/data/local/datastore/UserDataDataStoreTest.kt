package com.anshtya.movieinfo.data.local.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.anshtya.movieinfo.data.model.SelectedDarkMode
import com.anshtya.movieinfo.data.model.user.UserData
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class UserDataDataStoreTest {
    private lateinit var userPreferencesDataStore: UserPreferencesDataStore

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setUp() {
        userPreferencesDataStore = UserPreferencesDataStore(
            PreferenceDataStoreFactory.create {
                tmpFolder.newFile("user_data_test.preferences_pb")
            }
        )
    }

    @Test
    fun userData_defaultValues() = runTest {
        TestCase.assertEquals(
            UserData(
                useDynamicColor = false,
                includeAdultResults = false,
                darkMode = SelectedDarkMode.SYSTEM,
                hideOnboarding = false
            ),
            userPreferencesDataStore.userData.first()
        )
    }

    @Test
    fun useDynamicColorRoundTrip() = runTest {
        userPreferencesDataStore.setDynamicColorPreference(true)

        TestCase.assertEquals(
            true,
            userPreferencesDataStore.userData.first().useDynamicColor
        )
    }

    @Test
    fun includeAdultResultsRoundTrip() = runTest {
        userPreferencesDataStore.setAdultResultPreference(true)

        TestCase.assertEquals(
            true,
            userPreferencesDataStore.userData.first().includeAdultResults
        )
    }

    @Test
    fun userData_reflectsAllPreferencesTogether() = runTest {
        userPreferencesDataStore.setDynamicColorPreference(true)
        userPreferencesDataStore.setAdultResultPreference(true)
        userPreferencesDataStore.setDarkModePreference(SelectedDarkMode.DARK)
        userPreferencesDataStore.setHideOnboarding(true)

        TestCase.assertEquals(
            UserData(
                useDynamicColor = true,
                includeAdultResults = true,
                darkMode = SelectedDarkMode.DARK,
                hideOnboarding = true
            ),
            userPreferencesDataStore.userData.first()
        )
    }
}
