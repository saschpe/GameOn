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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = "1.8"

    sourceSets.forEach { it.java.srcDir("src/${it.name}/kotlin") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":domain"))
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("com.google.android.material:material:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.8")
    implementation("saschpe.log4k:log4k:0.1.7")

    testImplementation("androidx.test:core-ktx:1.2.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.1")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("org.robolectric:robolectric:4.3.1") {
        // https://github.com/robolectric/robolectric/issues/4621
        exclude(group = "com.google.auto.service", module = "auto-service")
    }
}
