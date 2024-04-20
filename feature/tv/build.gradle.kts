plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.movieinfo.feature.tv"
}

dependencies {
    implementation(projects.data)

    implementation(libs.androidx.paging.compose)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}