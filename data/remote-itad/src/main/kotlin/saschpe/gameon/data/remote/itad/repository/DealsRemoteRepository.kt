package saschpe.gameon.data.remote.itad.repository

import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import saschpe.gameon.data.core.asResult
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.data.remote.itad.Api
import saschpe.gameon.data.remote.itad.model.Meta

class DealsRemoteRepository(private val api: Api) {
    suspend fun list(
        region: String = "eu1",
        country: String = "de",
        shops: List<String> = DEFAULT_STORES,
        limit: Int = 500,
        offset: Int = 0,
        sort: String = "price:asc",
    ) = asResult {
        api.get<DealResponse>("deals/v2") {
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("region", region)
            parameter("country", country)
            parameter("shops", shops.joinToString(separator = ","))
            parameter("sort", sort)
        }
    }

    @Serializable
    data class DealResponse(@SerialName(".meta") val meta: Meta, val data: Data) {
        @Serializable
        data class Data(val count: Int, @SerialName("list") val offers: List<Offer>)
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
