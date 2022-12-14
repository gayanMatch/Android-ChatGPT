@file:Suppress("UnstableApiUsage")
pluginManagement {
  includeBuild("build-logic")
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    maven(url = "https://jitpack.io")
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    maven(url = "https://jitpack.io")
  }
}
rootProject.name = "chatgpt-android"
include(":app")
include(":core-model")
include(":core-network")
include(":core-preferences")
include(":core-data")
include(":core-designsystem")
include(":core-navigation")
include(":feature-chat")
include(":feature-login")
include(":benchmark")