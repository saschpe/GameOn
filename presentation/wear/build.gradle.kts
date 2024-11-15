plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.play)
}

dependencies {
    compileOnly("com.google.android.wearable:wearable:2.9.0")

    implementation(project(":domain"))
    implementation(project(":presentation:common"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.wear)
    implementation("com.google.android.gms:play-services-wearable:18.2.0")
    implementation(libs.google.material)
    implementation("com.google.android.support:wearable:2.9.0")
    implementation(libs.google.firebase.crashlytics)
    implementation("de.peilicke.sascha:android-versioninfo:2.2.0")
    implementation(libs.log4k)
    implementation(libs.coil)

    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.espresso.contrib)
    testImplementation(libs.androidx.espresso.intents)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.mockk)
}

android {
    namespace = "saschpe.gameon.wear"

    defaultConfig {
        applicationId = "saschpe.gameon"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = 28
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 211000333
        versionName = "0.3.33"
        base.archivesName = "$applicationId-wear-$versionName"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    signingConfigs {
        register("default") {
            keyAlias = Secrets.Signing.Key.ALIAS
            keyPassword = Secrets.Signing.Key.PASSWORD
            storeFile = Secrets.Signing.Store.file
            storePassword = Secrets.Signing.Store.PASSWORD
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug" // Allow installation in parallel to release builds
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("default")
        }
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }
}

kotlin.jvmToolchain(libs.versions.java.get().toInt())

// play {
//    defaultToAppBundles.set(true)
//    serviceAccountCredentials.set(file("$rootDir/config/play-publishing-api.json"))
//    track.set("alpha")
// }
