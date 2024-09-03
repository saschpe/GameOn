package saschpe.gameon.data.remote.itad.repository

import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import saschpe.gameon.data.core.asResult
import saschpe.gameon.data.core.model.Currency
import saschpe.gameon.data.core.model.Store
import saschpe.gameon.data.remote.itad.Api

class WebRemoteRepository(
    private val api: Api
) {
    suspend fun regions() = asResult {
        api.get<RegionsResponse>("web/regions")
    }

    suspend fun stores(region: String = "eu1", country: String = "de") = asResult {
        api.get<StoresResponse>("web/stores") {
            parameter("region", region)
            parameter("country", country)
        }
    }

    @Serializable
    data class RegionsResponse(
        val data: HashMap<String, Region>
    ) {
        @Serializable
        data class Region(
            val countries: List<String>,
            val currency: Currency
        )
    }

    @Serializable
    data class StoresResponse(val data: List<Store>)
}
