plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.preference.ktx)
    implementation(libs.google.material)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.log4k)

    testImplementation(libs.androidx.core.ktx)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
}

android {
    namespace = "saschpe.gameon.common"

    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility(libs.versions.java.get())
        targetCompatibility(libs.versions.java.get())
    }
}
