plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.feature.you"
}

dependencies {
    implementation(projects.data)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}