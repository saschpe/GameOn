package saschpe.gameon.data.remote.repository

import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.data.remote.Api
import saschpe.gameon.data.remote.model.Meta

class SearchRemoteRepository(
    private val api: Api
) : RemoteRepository {
    suspend fun search(
        query: String,
        region: String = "eu1",
        country: String = "de",
        shops: List<String> = listOf("steam"),
        limit: Int = 50,
        offset: Int = 0
    ): Result<SearchResponse> = asResult {
        api.get<SearchResponse>("search/search/") {
            parameter("q", query)
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("region", region)
            parameter("country", country)
            parameter("shops", shops.joinToString(separator = ","))
        }
    }

    @Serializable
    data class SearchResponse(
        @SerialName(".meta") val meta: Meta,
        val data: Data
    ) {
        @Serializable
        data class Data(val list: List<Offer>)
    }
}