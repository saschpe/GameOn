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
    api(project(":data:core")) {
        // TODO: Consider domain-specific models to hide underlying layers. Currently, considered overkill.
    }
    api(project(":data:remote-firebase")) {
        // TODO: Consider domain-specific models to hide underlying layers. Currently, considered overkill.
    }

    implementation(kotlin("stdlib"))
    implementation(project(":data:local"))
    implementation(project(":data:remote-itad"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    testImplementation("androidx.test:core-ktx:1.3.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.2")
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("org.robolectric:robolectric:4.5.1")
}
