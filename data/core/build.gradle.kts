plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.3.61"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib", "1.3.61"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")

    testImplementation(kotlin("test-junit", "1.3.61"))
}
