plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.movieinfo.sync"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(projects.data)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)

    api(libs.androidx.hilt.work)
    api(libs.androidx.work.runtime.ktx)
    ksp(libs.hilt.compiler)
}