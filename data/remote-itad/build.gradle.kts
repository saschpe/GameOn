plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":data:core"))
    implementation("io.ktor:ktor-client-android:1.5.4")
    implementation("io.ktor:ktor-client-logging-jvm:1.5.4")
    implementation("io.ktor:ktor-client-serialization-jvm:1.5.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.3")

    testImplementation(kotlin("test-junit"))
    testImplementation("io.ktor:ktor-client-mock-jvm:1.5.4")
    testImplementation("io.mockk:mockk:1.11.0")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.3")
}
