@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
    }

    resolutionStrategy {
        eachPlugin {
            // Custom resolution rules for Gradle plugins that don't support the `plugins {}` DSL.
            if (requested.id.id == "com.google.android.gms.oss-licenses-plugin") {
                useModule("com.google.android.gms:oss-licenses-plugin:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
    }
}

rootProject.name = "GameOn"

include(
    ":data:core",
    ":data:local",
    ":data:remote-firebase",
    ":data:remote-itad",
    ":domain",
    ":presentation:common",
    ":presentation:mobile",
    ":presentation:wear"
)
