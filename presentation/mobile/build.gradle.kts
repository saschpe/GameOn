plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.google.oss.licenses)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.play)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":presentation:common"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.browser)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.fragment.testing)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.taptargetview)
    implementation(libs.play.services.ads)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.oss.licenses)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.perf.ktx)
    implementation(libs.google.material)
    implementation("de.peilicke.sascha:android-customtabs:3.0.3")
    implementation("de.peilicke.sascha:android-social-fragment:2.1.1")
    implementation("de.peilicke.sascha:android-versioninfo:2.2.0")
    implementation(libs.log4k)
    implementation(libs.coil)
    implementation("me.zhanghai.android.materialprogressbar:library:1.6.1")
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.espresso.contrib)
    testImplementation(libs.androidx.espresso.idling.resource)
    testImplementation(libs.androidx.espresso.intents)
    testImplementation(libs.androidx.espresso.web)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.androidx.work.testing)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.espresso.web)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.mockk)
}

kotlin.jvmToolchain(libs.versions.java.get().toInt())

android {
    namespace = "saschpe.gameon.mobile"

    defaultConfig {
        applicationId = "saschpe.gameon"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 210000333
        versionName = "0.3.33"
        multiDexEnabled = true
        base.archivesName = "$applicationId-mobile-$versionName"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
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

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }
}

play {
    defaultToAppBundles.set(true)
    serviceAccountCredentials.set(file("$rootDir/config/play-publishing-api.json"))
    track.set("alpha")
}
