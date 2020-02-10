plugins {
    id("com.android.application")
    id("com.github.triplet.play") version "2.6.2"
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.firebase.crashlytics")
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
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 210000307
        versionName = "0.3.7"
        multiDexEnabled = true
        base.archivesBaseName = "$applicationId-mobile-$versionName"
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("default")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = "1.8"

    // https://youtrack.jetbrains.com/issue/KT-9770
    packagingOptions.exclude("**/*.kotlin_*")

    sourceSets.forEach { it.java.srcDir("src/${it.name}/kotlin") }

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.61"))
    implementation(project(":domain"))
    implementation(project(":presentation:common"))
    implementation("androidx.browser:browser:1.2.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.fragment:fragment-ktx:1.2.1")
    implementation("androidx.fragment:fragment-testing:1.2.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.1")
    implementation("androidx.preference:preference-ktx:1.1.0")
    implementation("androidx.work:work-runtime-ktx:2.3.1")
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.13.0")
    implementation("com.google.android.gms:play-services-auth:17.0.0")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
    implementation("com.google.firebase:firebase-analytics:17.2.2")
    implementation("com.google.firebase:firebase-common-ktx:19.3.0")
    implementation("com.google.firebase:firebase-crashlytics:17.0.0-beta01")
    implementation("com.google.android.material:material:1.1.0")
    implementation("io.coil-kt:coil:0.9.5")
    implementation("me.zhanghai.android.materialprogressbar:library:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")
    implementation("saschpe.android:customtabs:3.0.1")
    implementation("saschpe.android:social-fragment:2.0.3")
    implementation("saschpe.android:versioninfo:2.1.2")
    implementation("saschpe.log4k:log4k:0.1.6")
    debugRuntimeOnly("ch.qos.logback:logback-classic:1.2.3")
    constraints {
        implementation("androidx.test:core:1.2.0")
        implementation("androidx.test:monitor:1.2.0")
    }

    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.test.espresso:espresso-core:3.2.0")
    testImplementation("androidx.test.espresso:espresso-contrib:3.2.0")
    testImplementation("androidx.test.espresso:espresso-idling-resource:3.2.0")
    testImplementation("androidx.test.espresso:espresso-intents:3.2.0")
    testImplementation("androidx.test.espresso:espresso-web:3.2.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.1")
    testImplementation("androidx.work:work-testing:2.3.1")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.robolectric:robolectric:4.3.1") {
        // https://github.com/robolectric/robolectric/issues/4621
        exclude(group = "com.google.auto.service", module = "auto-service")
    }

    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-web:3.2.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.1")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    androidTestImplementation("io.mockk:mockk-android:1.9.2")
}

play {
    defaultToAppBundles = true
    serviceAccountCredentials = file("$rootDir/config/play-publishing-api.json")
    track = "alpha"
}
