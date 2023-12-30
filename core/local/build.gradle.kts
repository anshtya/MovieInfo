plugins {
    id("movieinfo.android.library")
    id("movieinfo.android.hilt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.example.core.local"

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    api(projects.core.model)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.datastore.preferences)
    api(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.runtime)
    implementation(libs.protobuf.kotlin.lite)

    ksp(libs.androidx.room.compiler)
}