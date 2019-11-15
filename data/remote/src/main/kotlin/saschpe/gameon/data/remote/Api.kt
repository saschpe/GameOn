package saschpe.gameon.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.features.UserAgent
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

class Api(
    userAgent: String,
    val apiKey: String = "d1ac7e96fdeb83528a7489be031597272430d922"
) {
    val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json.nonstrict)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        install(UserAgent) { agent = userAgent }
    }

    fun fullUrl(path: String) = "${API_BASE_URL}$path"

    suspend inline fun <reified T> get(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T = client.get(fullUrl(path)) { parameter("key", apiKey); block() }

    suspend inline fun <reified T> put(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T = client.put(fullUrl(path)) { parameter("key", apiKey); block() }

    suspend inline fun <reified T> post(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T = client.post(fullUrl(path)) { parameter("key", apiKey); block() }

    suspend inline fun <reified T> delete(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T = client.delete(fullUrl(path)) { parameter("key", apiKey); block() }

    companion object {
        private const val API_BASE_URL = "https://api.isthereanydeal.com/v01/"
    }
}

fun HttpRequestBuilder.headerAuthorization(token: String) =
    header(HttpHeaders.Authorization, "Bearer $token")

fun HttpRequestBuilder.headerContentTypeJson() =
    header(HttpHeaders.ContentType, ContentType.Application.Json)

fun HttpRequestBuilder.headerAcceptJson() =
    header(HttpHeaders.Accept, ContentType.Application.Json)
