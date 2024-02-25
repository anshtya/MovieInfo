plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.feature.details"
}

dependencies {
    implementation(projects.data)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}