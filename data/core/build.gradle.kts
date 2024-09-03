plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.core)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.kotlinx.coroutines.core)
}
