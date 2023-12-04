plugins {
    id("movieinfo.android.application")
    id("movieinfo.android.application.compose")
    id("movieinfo.android.hilt")
}

android {
    namespace = "com.anshtya.movieinfo"

    defaultConfig {
        applicationId = "com.anshtya.movieinfo"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.feature.home)

    implementation(libs.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.compose.material.iconsExtended)
    implementation(libs.coil.kt)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)
    testImplementation(libs.junit)
}