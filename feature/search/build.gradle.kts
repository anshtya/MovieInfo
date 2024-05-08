plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.movieinfo.feature.search"
}

dependencies {
    implementation(projects.data)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
