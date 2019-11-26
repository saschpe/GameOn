package saschpe.gameon.data.remote.repository

import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.data.core.model.GamePrice
import saschpe.gameon.data.remote.Api
import saschpe.gameon.data.remote.model.Meta

class GameRemoteRepository(
    private val api: Api
) : RemoteRepository {
    /**
     * Get info about game.
     */
    suspend fun info(plains: List<String>): Result<GameInfoResponse> = asResult {
        api.get<GameInfoResponse>("game/info") {
            parameter("plains", plains.joinToString(separator = ","))
        }
    }

    /**
     * Get basic information about game's prices: best current price, historical lowest price, and bundles in which the game is included.
     */
    suspend fun overview(
        plains: List<String>,
        region: String = "eu1",
        country: String = "de",
        shops: List<String> = listOf(),
        allowed: List<String> = listOf("steam", "gog")
    ): Result<GameOverviewResponse> = asResult {
        api.get<GameOverviewResponse>("game/overview") {
            parameter("plains", plains.joinToString(separator = ","))
            parameter("region", region)
            parameter("country", country)
            parameter("shops", shops.joinToString(separator = ","))
            parameter("allowed", allowed.joinToString(separator = ","))
        }
    }

    /**
     * Get all current prices for one or more selected games. Use region and country to get more accurate results.
     */
    suspend fun prices(
        plains: List<String>,
        region: String = "eu1",
        country: String = "de",
        shops: List<String> = listOf(),
        added: Long = 0
    ): Result<GamePricesResponse> = asResult {
        api.get<GamePricesResponse>("game/prices") {
            parameter("plains", plains.joinToString(separator = ","))
            parameter("region", region)
            parameter("country", country)
            parameter("shops", shops.joinToString(separator = ","))
            parameter("added", added)
        }
    }

    @Serializable
    data class GameInfoResponse(
        val data: HashMap<String, GameInfo>
    )

    @Serializable
    data class GameOverviewResponse(
        @SerialName(".meta") val meta: Meta,
        val data: HashMap<String, GameOverview>
    ) {
        @Serializable
        data class Meta(
            val region: String,
            val country: String,
            val currency: String
        )
    }

    @Serializable
    data class GamePricesResponse(
        @SerialName(".meta") val meta: Meta,
        val data: HashMap<String, GamePrice>
    )
}