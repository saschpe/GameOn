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