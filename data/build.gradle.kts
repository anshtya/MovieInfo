plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.movieinfo.data"
}

dependencies {
    implementation(projects.core.database)
    implementation(projects.core.network)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}