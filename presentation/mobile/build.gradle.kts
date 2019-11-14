plugins {
    id("com.android.application")
    id("com.github.triplet.play") version "2.5.0"
    id("com.google.android.gms.oss-licenses-plugin")
    // id("com.google.gms.google-services")
    kotlin("android")
    kotlin("android.extensions")
}

repositories {
    google()
    jcenter()
    mavenCentral()
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "saschpe.gamesale"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 210000101
        versionName = "0.1.1"
        multiDexEnabled = true
        base.archivesBaseName = "$applicationId-mobile-$versionName"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
            isTestCoverageEnabled = true
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
        setSourceCompatibility(1.8)
        setTargetCompatibility(1.8)
    }

    jacoco.version = "0.8.4"

    // https://youtrack.jetbrains.com/issue/KT-9770
    packagingOptions.exclude("**/*.kotlin_*")

    sourceSets {
        // Increase Android Studio Kotlin compatibility
        named("androidTest") { java.srcDirs("src/androidTest/kotlin") }
        named("main") { java.srcDirs("src/main/kotlin") }
        named("test") { java.srcDirs("src/test/kotlin") }
    }

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.50"))
    implementation(project(":domain"))
    implementation(project(":presentation:common"))
    implementation("androidx.browser:browser:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.1.0")
    implementation("androidx.fragment:fragment-testing:1.1.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-rc02")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.1.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.1.0")
    implementation("androidx.preference:preference-ktx:1.1.0")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.13.0")
    implementation("com.google.android.gms:play-services-auth:17.0.0")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
    implementation("com.google.firebase:firebase-ads:18.3.0")
    implementation("com.google.firebase:firebase-auth:19.1.0")
    implementation("com.google.firebase:firebase-common-ktx:19.3.0")
    implementation("com.google.android.material:material:1.1.0-beta02")
    implementation("de.hdodenhof:circleimageview:3.0.1")
    implementation("io.coil-kt:coil:0.8.0")
    implementation("io.github.jupf.staticlog:staticlog:2.2.0")
    implementation("me.zhanghai.android.materialprogressbar:library:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.2")
    implementation("saschpe.android:customtabs:2.0.3")
    implementation("saschpe.android:social-fragment:2.0.3")
    implementation("saschpe.android:versioninfo:2.1.2")
    implementation("saschpe.log4k:log4k:0.1.0")
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
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }
