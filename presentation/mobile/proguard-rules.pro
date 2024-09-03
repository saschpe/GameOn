# KotlinX.Serialization
# See https://github.com/Kotlin/kotlinx.serialization#androidjvm
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt

-keep,includedescriptorclasses class saschpe.gameon.data.local.model.**$$serializer { *; }
-keepclassmembers class saschpe.gameon.data.local.model.** {
    *** Companion;
}
-keepclasseswithmembers class saschpe.gameon.data.local.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class saschpe.gameon.data.remote.**$$serializer { *; }
-keepclassmembers class saschpe.gameon.data.remote.repository.** {
    *** Companion;
}
-keepclasseswithmembers class saschpe.gameon.data.remote.** {
    kotlinx.serialization.KSerializer serializer(...);
}


# Fragments refrenced in XML headers
-keep class saschpe.gameon.mobile.settings.preferences.** { *; }


# Crashlytics
# See https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# TODO: Remove once AS 4.0 / IntelliJ 2020.1 is used:
# https://issuetracker.google.com/issues/154538242
# https://issuetracker.google.com/issues/79667498
# https://issuetracker.google.com/issues/79631818
-keep class androidx.navigation.fragment.NavHostFragment

# See https://youtrack.jetbrains.com/issue/KTOR-5528/Missing-class-warning-when-using-R8-with-ktor-client-in-android-application
-dontwarn org.slf4j.impl.StaticLoggerBinder