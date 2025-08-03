plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
    id("movieinfo.compose")
}

android {
    namespace = "com.anshtya.movieinfo.feature"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.ui.tooling.preview)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.ui.tooling)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.ui.test.junit4)
}