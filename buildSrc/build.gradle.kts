plugins {
    kotlin("jvm") version "1.3.60"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks {
    val ensureSecretsExist by registering {
        val secretFile = File("$rootDir/src/main/kotlin/Secrets.kt")

        description = "Ensures that $secretFile exists"
        doFirst {
            if (!secretFile.exists()) {
                secretFile.writeText(
                    """
import java.io.File

object Secrets {
    object Signing {
        object Key {
            const val alias = "androiddebugkey"
            const val password = "android"
        }

        object Store {
            val file = File("${'$'}{System.getProperty("user.home")}/.android/debug.keystore")
            const val password = "android"
        }
    }
}

""".trimIndent()
                )
            }
        }
    }
    named("assemble") { dependsOn(ensureSecretsExist) }
}
