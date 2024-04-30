plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.data"
}

dependencies {
    api(projects.core.model)

    implementation(projects.core.local)
    implementation(projects.core.network)

    testImplementation(projects.core.testing)
}