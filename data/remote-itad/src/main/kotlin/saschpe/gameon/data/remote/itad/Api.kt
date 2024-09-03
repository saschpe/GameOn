package saschpe.gameon.data.remote.itad

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class Api(
    userAgent: String,
    val apiKey: String
) {
    @Suppress("EXPERIMENTAL_API_USAGE")
    val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        install(UserAgent) { agent = userAgent }
    }

    fun fullUrl(path: String) = "$API_URL$API_BASE_PATH$path"

    suspend inline fun get(
        path: String, block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = client.get {
        url(fullUrl(path))
        parameter("key", apiKey)
        block()
    }

    companion object {
        private const val API_URL = "https://api.isthereanydeal.com/"
        private const val API_BASE_PATH = "v01/"
    }
}
