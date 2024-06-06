plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.movieinfo.feature.you"
}

dependencies {
    implementation(projects.data)

    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}