plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.movieinfo.sync"
}

dependencies {
    implementation(projects.data)

    api(libs.androidx.hilt.work)
    api(libs.androidx.work.runtime.ktx)
    ksp(libs.androidx.hilt.compiler)
}