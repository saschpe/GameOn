plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.play)
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

    signingConfigs {
        register("default") {
            keyAlias = Secrets.Signing.Key.alias
            keyPassword = Secrets.Signing.Key.password
            storeFile = Secrets.Signing.Store.file
            storePassword = Secrets.Signing.Store.password
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

    compileOptions {
        sourceCompatibility(libs.versions.java.get())
        targetCompatibility(libs.versions.java.get())
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
        textReport = project.hasProperty("isCI")
    }

    packagingOptions.resources.excludes.add("**/*.kotlin_*") // https://youtrack.jetbrains.com/issue/KT-9770

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }
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

//play {
//    defaultToAppBundles.set(true)
//    serviceAccountCredentials.set(file("$rootDir/config/play-publishing-api.json"))
//    track.set("alpha")
//}
