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

    testImplementation(projects.core.testing)
    androidTestImplementation(projects.core.testing)

    api(libs.androidx.hilt.work)
    api(libs.androidx.work.runtime.ktx)

    ksp(libs.androidx.hilt.compiler)
}