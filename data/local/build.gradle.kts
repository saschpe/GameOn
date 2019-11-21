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

    sourceSets {
        named("androidTest") {
            java.srcDirs("src/androidTest/kotlin")
            assets.srcDirs("src/main/schemas")
        }
        named("main") { java.srcDirs("src/main/kotlin") }
        named("test") { java.srcDirs("src/test/kotlin") }
    }
}

dependencies {
    kapt("androidx.room:room-compiler:2.2.1")

    implementation(kotlin("stdlib-jdk8", "1.3.60"))
    implementation(project(":data:core"))
    implementation("androidx.room:room-runtime:2.2.1")
    implementation("androidx.room:room-ktx:2.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")

    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.room:room-testing:2.2.1")
    testImplementation("androidx.test:core-ktx:1.2.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.1")
    testImplementation("io.mockk:mockk:1.9.3")
}
