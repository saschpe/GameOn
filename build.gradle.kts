buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.3")
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.2")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.0.0")
        classpath("com.google.firebase:perf-plugin:1.3.1")
    }
}

plugins {
    id("com.diffplug.gradle.spotless") version "3.28.0"
    id("com.github.ben-manes.versions") version "0.28.0"
    kotlin("jvm") version "1.3.72"
}

repositories {
    jcenter()
}

spotless {
    format("misc") {
        target("**/*.gradle", "**/*.md", "**/.gitignore")
        trimTrailingWhitespace()
        endWithNewline()
    }
    freshmark {
        propertiesFile("gradle.properties")
    }
    kotlin {
        target("*/src/**/*.kt")
        ktlint().userData(mapOf("disabled_rules" to "no-wildcard-imports"))
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        fun isStable(version: String) = Regex("^[0-9,.v-]+(-r)?$").matches(version)

        !isStable(candidate.version) && isStable(currentVersion)
    }
}
