plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.movieinfo.feature.auth"
}

dependencies {
    implementation(projects.data)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}