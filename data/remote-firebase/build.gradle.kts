plugins {
    id("com.android.library")
    kotlin("android")
}

repositories {
    mavenCentral()
    google()
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets.forEach { it.java.srcDir("src/${it.name}/kotlin") }
}

dependencies {
    api("com.google.firebase:firebase-auth:21.0.0") {
        // TODO: Introduce domain models and map accordingly
    }

    implementation(project(":data:core"))
    implementation("de.peilicke.sascha:log4k:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.4.2")

    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.11.0")
}
