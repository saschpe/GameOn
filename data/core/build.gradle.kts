plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.0"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")

    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
}
