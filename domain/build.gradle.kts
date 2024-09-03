plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

dependencies {
    api(project(":data:core")) {
        // TODO: Consider domain-specific models to hide underlying layers. Currently, considered overkill.
    }
    api(project(":data:remote-firebase")) {
        // TODO: Consider domain-specific models to hide underlying layers. Currently, considered overkill.
    }

    implementation(project(":data:local"))
    implementation(project(":data:remote-itad"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.androidx.core.ktx)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
}

android {
    namespace = "saschpe.gameon.domain"

    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
