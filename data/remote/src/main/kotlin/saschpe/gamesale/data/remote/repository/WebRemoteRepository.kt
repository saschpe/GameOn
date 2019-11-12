package saschpe.gamesale.data.remote.repository

import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable
import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.Currency
import saschpe.gamesale.data.core.model.Store
import saschpe.gamesale.data.remote.Api

class WebRemoteRepository(
    private val api: Api
) : RemoteRepository {
    suspend fun regions(): Result<RegionsResponse> = asResult {
        api.get<RegionsResponse>("web/regions")
    }

    suspend fun stores(region: String = "eu1", country: String = "de"): Result<StoresResponse> =
        asResult {
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