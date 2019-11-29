package saschpe.gameon.data.remote.repository

import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import saschpe.gameon.data.core.asResult
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.data.remote.Api
import saschpe.gameon.data.remote.model.Meta

class DealsRemoteRepository(
    private val api: Api
) {
    suspend fun list(
        region: String = "eu1",
        country: String = "de",
        shops: List<String> = DEFAULT_STORES,
        limit: Int = 150,
        offset: Int = 0,
        sort: String = "price:asc"
    ) = asResult {
        api.get<DealResponse>("deals/list") {
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("region", region)
            parameter("country", country)
            parameter("shops", shops.joinToString(separator = ","))
            parameter("sort", sort)
        }
    }

    @Serializable
    data class DealResponse(
        @SerialName(".meta") val meta: Meta,
        val data: Data
    ) {
        @Serializable
        data class Data(
            val count: Int,
            val list: List<Offer>
        )
    }

    companion object {
        val DEFAULT_STORES = listOf(
            "steam",
            "gamersgate",
            "gamesplanet",
            "greenmangaming",
            "gog",
            "dotemu",
            "amazonus",
            "nuuvem"
        )
    }
}