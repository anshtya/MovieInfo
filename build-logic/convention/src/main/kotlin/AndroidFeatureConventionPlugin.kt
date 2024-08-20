import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import com.anshtya.movieinfo.findLibrary

class AndroidFeatureConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("movieinfo.android.library")
                apply("movieinfo.android.library.compose")
                apply("movieinfo.android.hilt")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                dependencies {
                    add("implementation", project(":core:ui"))

                    add("implementation", findLibrary("androidx-lifecycle-runtime-compose"))
                    add("implementation", findLibrary("androidx-hilt-navigation-compose"))
                }
            }
        }
    }
}