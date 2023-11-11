@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.library.compose")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.home"
}

dependencies {
    implementation(project(":data"))
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}