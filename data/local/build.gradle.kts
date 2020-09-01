plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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

        kapt.arguments {
            arg("room.schemaLocation", "$projectDir/src/main/schemas")
            arg("room.incremental", "true")
        }
    }

    sourceSets.forEach {
        it.java.srcDir("src/${it.name}/kotlin")
        it.assets.srcDir("src/${it.name}/schemas")
    }

    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    kapt("androidx.room:room-compiler:2.2.5")

    implementation(kotlin("stdlib"))
    implementation(project(":data:core"))
    implementation("androidx.room:room-runtime:2.2.5")
    implementation("androidx.room:room-ktx:2.2.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    testImplementation("androidx.room:room-testing:2.2.5")
    testImplementation("androidx.test.ext:junit-ktx:1.1.2")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("org.robolectric:robolectric:4.3.1") {
        // https://github.com/robolectric/robolectric/issues/4621
        exclude(group = "com.google.auto.service", module = "auto-service")
    }
}
