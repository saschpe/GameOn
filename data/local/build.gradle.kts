plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
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

    sourceSets {
        // Increase Android Studio Kotlin compatibility
        named("androidTest") { java.srcDirs("src/androidTest/kotlin") }
        named("main") { java.srcDirs("src/main/kotlin") }
        named("test") { java.srcDirs("src/test/kotlin") }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.50"))
    implementation(project(":data:core"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")

    testImplementation("androidx.test:core-ktx:1.2.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.1")
    testImplementation("io.mockk:mockk:1.9.3")
}
