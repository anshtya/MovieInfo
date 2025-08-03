plugins {
    id("movieinfo.android.library")
    id("movieinfo.compose")
}

android {
    namespace = "com.anshtya.movieinfo.core.ui"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(libs.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.ui.test.junit4)

    debugImplementation(libs.ui.test.manifest)
}