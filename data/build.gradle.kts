plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.data"
}

dependencies {
    implementation(projects.core.local)
    api(projects.core.model)
    implementation(projects.core.network)

    testImplementation(libs.junit)

    implementation(libs.androidx.paging.runtime)
}