pluginManagement {
    plugins {
        id("com.github.triplet.play") version "2.8.0"
        kotlin("plugin.serialization") version "1.4.31"
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
