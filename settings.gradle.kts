@file:Suppress("UnstableApiUsage")

include(":app")
include(":ComposeScrollbars")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        includeBuild("plugins")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ComposeScrollbarsProject"
