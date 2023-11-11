import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.deps
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")