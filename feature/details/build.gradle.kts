plugins {
    id("movieinfo.android.feature")
}

android {
    namespace = "com.anshtya.feature.details"
}

dependencies {
    implementation(projects.data)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}