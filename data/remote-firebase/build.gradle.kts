plugins {
    id("com.android.library")
    kotlin("android")
}

repositories {
    jcenter()
    google()
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets.forEach { it.java.srcDir("src/${it.name}/kotlin") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":data:core"))
    api("com.google.firebase:firebase-auth:19.3.2") {
        // TODO: Introduce domain models and map accordingly
    }
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.4.2")
    implementation("saschpe.log4k:log4k:0.1.7")

    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.10.6")
}
