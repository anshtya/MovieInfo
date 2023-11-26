import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import util.getLibrary

class AndroidLibraryComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension>() {
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = "1.4.3"
                }
                dependencies {
                    val bom = getLibrary("compose-bom")
                    add("implementation", platform(bom))
                    add("implementation", getLibrary("material3"))
                    add("implementation", getLibrary("ui-tooling-preview"))
                    add("implementation", getLibrary("androidx-lifecycle-runtime-compose"))
                    add("debugImplementation", getLibrary("ui-tooling"))
                }
            }
        }
    }
}