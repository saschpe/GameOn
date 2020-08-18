plugins {
    id("com.android.application")
    id("com.github.triplet.play") version "2.8.0"
    id("com.google.firebase.crashlytics")
    // id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("android.extensions")
}

repositories {
    jcenter()
    google()
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "saschpe.gameon"
        minSdkVersion(28)
        targetSdkVersion(29)
        versionCode = 211000331
        versionName = "0.3.31"
        base.archivesBaseName = "$applicationId-wear-$versionName"
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
        named("debug") {
            applicationIdSuffix = ".debug" // Allow installation in parallel to release builds
        }
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("default")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = "1.8"
    lintOptions {
        isAbortOnError = false
        textReport = project.hasProperty("isCI")
        textOutput("stdout")
    }
    packagingOptions.exclude("**/*.kotlin_*") // https://youtrack.jetbrains.com/issue/KT-9770

    sourceSets.forEach { it.java.srcDir("src/${it.name}/kotlin") }

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    compileOnly("com.google.android.wearable:wearable:2.7.0")

    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":domain"))
    implementation(project(":presentation:common"))
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.wear:wear:1.0.0")
    implementation("com.google.android.gms:play-services-wearable:17.0.0")
    implementation("com.google.android.material:material:1.2.0")
    implementation("com.google.android.support:wearable:2.7.0")
    implementation("com.google.firebase:firebase-crashlytics:17.1.1")
    implementation("io.coil-kt:coil:0.11.0")
    implementation("saschpe.android:versioninfo:2.1.2")
    implementation("saschpe.log4k:log4k:0.1.7")

    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.test.espresso:espresso-core:3.2.0")
    testImplementation("androidx.test.espresso:espresso-contrib:3.2.0")
    testImplementation("androidx.test.espresso:espresso-intents:3.2.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.1")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("org.robolectric:robolectric:4.3.1") {
        // https://github.com/robolectric/robolectric/issues/4621
        exclude(group = "com.google.auto.service", module = "auto-service")
    }

    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.2.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.1")
    androidTestImplementation("io.mockk:mockk-android:1.10.0")
}

play {
    defaultToAppBundles = true
    serviceAccountCredentials = file("$rootDir/config/play-publishing-api.json")
    track = "alpha"
}
