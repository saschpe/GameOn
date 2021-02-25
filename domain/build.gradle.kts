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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    api(project(":data:core")) {
        // TODO: Consider domain-specific models to hide underlying layers. Currently, considered overkill.
    }
    implementation(project(":data:local"))
    api(project(":data:remote-firebase")) {
        // TODO: Consider domain-specific models to hide underlying layers. Currently, considered overkill.
    }
    implementation(project(":data:remote-itad"))

    testImplementation("androidx.test:core-ktx:1.3.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.2")
    testImplementation("io.mockk:mockk:1.10.6")
    testImplementation("org.robolectric:robolectric:4.5.1")
}
