plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.library.compose")
}

android {
    namespace = "com.anshtya.movieinfo.core.ui"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(libs.compose.material.iconsExtended)
    implementation(libs.coil.kt.compose)

    androidTestImplementation(projects.core.testing)
}