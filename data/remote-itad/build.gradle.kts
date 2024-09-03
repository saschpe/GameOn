plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin.jvmToolchain(libs.versions.java.get().toInt())

dependencies {
    implementation(project(":data:core"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging.jvm)
    implementation(libs.ktor.client.serialization.kotlinx.json)
    implementation(libs.log4k)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.ktor.client.mock.jvm)
    testImplementation(libs.mockk)
}
