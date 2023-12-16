import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import util.getLibrary
import util.getVersion

class AndroidLibraryComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension>() {
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = getVersion("androidx-compose-compiler")
                }
                dependencies {
                    val bom = getLibrary("compose-bom")
                    add("implementation", platform(bom))
                    add("implementation", getLibrary("material3"))
                    add("implementation", getLibrary("ui-tooling-preview"))
                    add("implementation", getLibrary("androidx-lifecycle-runtime-compose"))
                    add("implementation", getLibrary("androidx-hilt-navigation-compose"))
                    add("debugImplementation", getLibrary("ui-tooling"))
                }
            }
        }
    }
}