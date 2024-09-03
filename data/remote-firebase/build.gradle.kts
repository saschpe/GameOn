plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

dependencies {
    api(platform(libs.firebase.bom))
    api(libs.firebase.auth.ktx) // TODO: Introduce domain models and map accordingly

    implementation(project(":data:core"))
    implementation(libs.log4k)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.mockk)
}

kotlin.jvmToolchain(libs.versions.java.get().toInt())

android {
    namespace = "saschpe.gameon.data.remote.firebase"

    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
