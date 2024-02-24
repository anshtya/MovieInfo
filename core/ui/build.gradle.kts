plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.library.compose")
}

android {
    namespace = "com.anshtya.core.ui"
}

dependencies {
    implementation(libs.compose.material.iconsExtended)
    implementation(libs.coil.kt.compose)
    implementation(libs.kotlinx.collections.immutable)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}