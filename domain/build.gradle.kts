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
    implementation(project(":data-local"))
    implementation(project(":data-remote"))
    implementation(kotlin("stdlib", "1.3.50"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.2")

    testImplementation("androidx.test:core-ktx:1.2.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.1")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.robolectric:robolectric:4.3.1") {
        // https://github.com/robolectric/robolectric/issues/4621
        exclude(group = "com.google.auto.service", module = "auto-service")
    }
}
