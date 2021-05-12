plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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
    kapt("androidx.room:room-compiler:2.2.6")

    implementation(kotlin("stdlib"))
    implementation(project(":data:core"))
    implementation("androidx.room:room-runtime:2.2.6")
    implementation("androidx.room:room-ktx:2.2.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    testImplementation("androidx.room:room-testing:2.2.6")
    testImplementation("androidx.test.ext:junit-ktx:1.1.2")
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("org.robolectric:robolectric:4.5.1")
}
