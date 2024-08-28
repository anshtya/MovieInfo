plugins {
    id("movieinfo.android.library")
}

android {
    namespace = "com.anshtya.movieinfo.core.testing"
}

dependencies {
    implementation(projects.data)

    api(libs.androidx.test.ext.junit)
    api(libs.androidx.test.runner)
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}