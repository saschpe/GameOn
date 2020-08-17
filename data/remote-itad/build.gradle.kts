plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.0"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":data:core"))
    implementation("io.ktor:ktor-client-android:1.3.2")
    implementation("io.ktor:ktor-client-logging-jvm:1.3.2")
    implementation("io.ktor:ktor-client-serialization-jvm:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")

    testImplementation(kotlin("test-junit"))
    testImplementation("io.ktor:ktor-client-mock-jvm:1.3.2")
    testImplementation("io.mockk:mockk:1.10.0")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.3")
}
