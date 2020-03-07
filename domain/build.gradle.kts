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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.4")
    // TODO: Consider domain-specific models to hide underlying layers. Currently, considered overkill.
    api(project(":data:core"))
    implementation(project(":data:local"))
    implementation(project(":data:remote-itad"))

    testImplementation("androidx.test:core-ktx:1.2.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.1")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.robolectric:robolectric:4.3.1") {
        // https://github.com/robolectric/robolectric/issues/4621
        exclude(group = "com.google.auto.service", module = "auto-service")
    }
}
