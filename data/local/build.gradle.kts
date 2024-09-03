plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.android)
}

dependencies {
    ksp(libs.androidx.room.compiler)

    implementation(project(":data:core"))
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
}

kotlin.jvmToolchain(libs.versions.java.get().toInt())

android {
    namespace = "saschpe.gameon.data.local"

    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    room {
        schemaDirectory("$projectDir/src/main/schemas")
    }

    sourceSets.forEach {
        it.assets.srcDir("src/${it.name}/schemas")
    }

    testCoverage.jacocoVersion = libs.versions.jacoco.get()
    testOptions.unitTests.isIncludeAndroidResources = true
}
