plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.feature.auth"
}

dependencies {
    implementation(projects.data)

    implementation(libs.compose.material.iconsExtended)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}