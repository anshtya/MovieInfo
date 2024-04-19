plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.library.compose")
}

android {
    namespace = "com.anshtya.core.ui"
}

dependencies {
    api(libs.coil.kt.compose)
    api(libs.compose.material.iconsExtended)

    implementation(libs.kotlinx.collections.immutable)

    androidTestImplementation(projects.core.testing)
}