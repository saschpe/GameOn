plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.3.60"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.60"))
    implementation(project(":data:core"))
    implementation("io.ktor:ktor-client-android:1.2.5")
    implementation("io.ktor:ktor-client-logging-jvm:1.2.5")
    implementation("io.ktor:ktor-client-serialization-jvm:1.2.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")

    testImplementation("junit:junit:4.12")
    testImplementation("io.mockk:mockk:1.9.3")
}
