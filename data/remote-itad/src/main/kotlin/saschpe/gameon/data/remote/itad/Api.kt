package saschpe.gameon.data.remote.itad

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import saschpe.log4k.Log

class Api(userAgent: String, val apiKey: String) {
    @Suppress("EXPERIMENTAL_API_USAGE")
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) = Log.info { message }
            }
        }
        install(UserAgent) { agent = userAgent }
    }

    fun fullUrl(path: String) = "$API_URL$path"

    suspend inline fun <reified T> get(path: String, block: HttpRequestBuilder.() -> Unit = {}): T = client.get {
        url(fullUrl(path))
        parameter("key", apiKey)
        block()
    }.body<T>()

    companion object {
        private const val API_URL = "https://api.isthereanydeal.com/"
    }
}
