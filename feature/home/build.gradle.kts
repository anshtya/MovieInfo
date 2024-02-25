plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.feature.home"
}

dependencies {
    implementation(projects.data)

    implementation(libs.androidx.paging.compose)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}