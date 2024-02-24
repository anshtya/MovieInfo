plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.feature.home"
}

dependencies {
    implementation(projects.data)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.paging.compose)
    implementation(libs.kotlinx.collections.immutable)
}