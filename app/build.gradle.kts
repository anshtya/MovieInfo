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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.feature.auth)
    implementation(projects.feature.details)
    implementation(projects.feature.home)
    implementation(projects.feature.search)
    implementation(projects.feature.you)
    implementation(projects.sync)

    implementation(libs.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.kt)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)
    testImplementation(libs.junit)
}