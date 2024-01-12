plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.library.compose")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.feature.details"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.data)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}