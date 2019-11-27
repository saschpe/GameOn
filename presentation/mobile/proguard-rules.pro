# https://github.com/Kotlin/kotlinx.serialization#androidjvm
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt

-keep,includedescriptorclasses class saschpe.gameon.data.local.model.**$$serializer { *; }
-keepclassmembers class saschpe.gameon.data.local.model.** {
    *** Companion;
}
-keepclasseswithmembers class saschpe.gameon.data.local.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class saschpe.gameon.data.remote.repository.**$$serializer { *; }
-keepclassmembers class saschpe.gameon.data.remote.repository.** {
    *** Companion;
}
-keepclasseswithmembers class saschpe.gameon.data.remote.repository.** {
    kotlinx.serialization.KSerializer serializer(...);
}