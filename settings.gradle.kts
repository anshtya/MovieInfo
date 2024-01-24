pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "MovieInfo"
include(":app")
include(":core:local")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":data")
include(":feature:auth")
include(":feature:details")
include(":feature:home")
include(":feature:search")
include(":feature:you")
include(":sync")
