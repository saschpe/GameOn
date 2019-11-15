plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.3.60"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.60"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
}
