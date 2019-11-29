package saschpe.gameon.data.remote.repository

import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import saschpe.gameon.data.core.asResult
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.data.remote.Api
import saschpe.gameon.data.remote.model.Meta

class SearchRemoteRepository(
    private val api: Api
) {
    suspend fun search(
        query: String,
        region: String = "eu1",
        country: String = "de",
        shops: List<String> = DEFAULT_STORES,
        limit: Int = 50,
        offset: Int = 0
    ) = asResult {
        api.get<SearchResponse>("search/search") {
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
        data class Data(
            @SerialName("list") val offers: List<Offer>
        )
    }

    companion object {
        val DEFAULT_STORES = listOf("steam")
    }
}