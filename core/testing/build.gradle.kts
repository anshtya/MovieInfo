plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.library.compose")
}

android {
    namespace = "com.anshtya.movieinfo.core.testing"
}

dependencies {
    implementation(projects.data)

    api(libs.junit)
    api(libs.ui.test.junit4)

    debugApi(libs.ui.test.manifest)
}