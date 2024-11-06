plugins {
    id("com.android.application")
    id("com.github.triplet.play")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("android.extensions")
}

repositories {
    mavenCentral()
    google()
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "saschpe.gameon"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 210000333
        versionName = "0.3.33"
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
        isCheckReleaseBuilds = false
        textReport = project.hasProperty("isCI")
        textOutput("stdout")
    }
    packagingOptions.exclude("**/*.kotlin_*") // https://youtrack.jetbrains.com/issue/KT-9770

    sourceSets.forEach { it.java.srcDir("src/${it.name}/kotlin") }

    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":presentation:common"))
    implementation("androidx.browser:browser:1.3.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.fragment:fragment-ktx:1.3.3")
    implementation("androidx.fragment:fragment-testing:1.3.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("androidx.work:work-runtime-ktx:2.5.0")
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.13.2")
    implementation("com.google.android.gms:play-services-ads:20.1.0")
    implementation("com.google.android.gms:play-services-auth:19.0.0")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
    implementation("com.google.firebase:firebase-analytics-ktx:19.0.0")
    implementation("com.google.firebase:firebase-crashlytics:18.0.0")
    implementation("com.google.firebase:firebase-perf:20.0.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("de.peilicke.sascha:android-customtabs:3.0.3")
    implementation("de.peilicke.sascha:android-social-fragment:2.1.1")
    implementation("de.peilicke.sascha:android-versioninfo:2.2.0")
    implementation("de.peilicke.sascha:log4k:1.0.1")
    implementation("io.coil-kt:coil:1.2.1")
    implementation("me.zhanghai.android.materialprogressbar:library:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")
    debugRuntimeOnly("ch.qos.logback:logback-classic:1.2.3")

    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.test.espresso:espresso-core:3.3.0")
    testImplementation("androidx.test.espresso:espresso-contrib:3.3.0")
    testImplementation("androidx.test.espresso:espresso-idling-resource:3.3.0")
    testImplementation("androidx.test.espresso:espresso-intents:3.3.0")
    testImplementation("androidx.test.espresso:espresso-web:3.3.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.2")
    testImplementation("androidx.work:work-testing:2.5.0")
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("org.robolectric:robolectric:4.5.1")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-web:3.3.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.2")
    androidTestImplementation("io.mockk:mockk-android:1.11.0")
}

play {
    defaultToAppBundles = true
    serviceAccountCredentials = file("$rootDir/config/play-publishing-api.json")
    track = "alpha"
}
