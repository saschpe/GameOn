package testing

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import kotlinx.serialization.json.Json

fun mockHttpClient(block: MockEngineConfig.() -> Unit = {}) = HttpClient(MockEngine) {
    installDefaultFeatures()
    engine {
        block()
    }
}

private fun HttpClientConfig<MockEngineConfig>.installDefaultFeatures() {
    install(JsonFeature) {
        serializer = KotlinxSerializer(
            Json.nonstrict
        )
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
}

val headersContentTypeJson =
    headersOf(HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))