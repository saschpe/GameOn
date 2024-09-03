package testing

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun mockHttpClient(block: MockEngineConfig.() -> Unit = {}) = HttpClient(MockEngine) {
    installDefaultFeatures()
    engine {
        block()
    }
}

private fun HttpClientConfig<MockEngineConfig>.installDefaultFeatures() {
    @Suppress("EXPERIMENTAL_API_USAGE")
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
}

val headersContentTypeJson = headersOf(
    HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())
)
