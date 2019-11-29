plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.3.61"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib", "1.3.61"))
    implementation(project(":data:core"))
    implementation("io.ktor:ktor-client-android:1.2.6")
    implementation("io.ktor:ktor-client-logging-jvm:1.2.6")
    implementation("io.ktor:ktor-client-serialization-jvm:1.2.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")

    testImplementation(kotlin("test-junit", "1.3.61"))
    testImplementation("io.ktor:ktor-client-mock-jvm:1.2.6")
    testImplementation("io.mockk:mockk:1.9.3")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.3")
}
