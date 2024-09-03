package saschpe.gameon.data.remote.itad.repository

import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import saschpe.gameon.data.core.asResult
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.data.core.model.GamePrice
import saschpe.gameon.data.remote.itad.Api
import saschpe.gameon.data.remote.itad.model.Meta

class GameRemoteRepository(
    private val api: Api,
) {
    /**
     * Get info about a game.
     *
     * @see <a href="https://itad.docs.apiary.io/#reference/game/info/get-info-about-game">API documentation</a>
     */
    suspend fun info(plains: List<String>) = asResult {
        api.get<GameInfoResponse>("games/info/v2") {
            parameter("plains", plains.joinToString(separator = ","))
        }
    }

    /**
     * Get basic information about game's prices: best current price, historical lowest price, and bundles in which the game is included.
     *
     * @see <a href="https://itad.docs.apiary.io/#reference/game/info/get-game-price-overview">API documentation</a>
     */
    suspend fun overview(
        plains: List<String>,
        region: String = "eu1",
        country: String = "de",
        shop: String = "stream",
        allowed: List<String> = listOf(),
    ) = asResult {
        api.get<GameOverviewResponse>("games/overview/v2") {
            parameter("plains", plains.joinToString(separator = ","))
            parameter("region", region)
            parameter("country", country)
            parameter("shop", shop)
            parameter("allowed", allowed.joinToString(separator = ","))
        }
    }

    /**
     * Get all current prices for one or more selected games. Use region and country to get more accurate results.
     *
     * @see <a href="https://itad.docs.apiary.io/#reference/game/prices/get-current-prices">API documentation</a>
     */
    suspend fun prices(
        plains: List<String>,
        region: String = "eu1",
        country: String = "de",
        shops: List<String> = listOf(),
        added: Long = 0,
    ) = asResult {
        api.get<GamePricesResponse>("games/prices/v2") {
            parameter("plains", plains.joinToString(separator = ","))
            parameter("region", region)
            parameter("country", country)
            parameter("shops", shops.joinToString(separator = ","))
            parameter("added", added)
        }
    }

    @Serializable
    data class GameInfoResponse(
        val data: HashMap<String, GameInfo>,
    )

    @Serializable
    data class GameOverviewResponse(
        @SerialName(".meta") val meta: Meta,
        val data: HashMap<String, GameOverview>,
    ) {
        @Serializable
        data class Meta(
            val region: String,
            val country: String,
            val currency: String,
        )
    }

    @Serializable
    data class GamePricesResponse(
        @SerialName(".meta") val meta: Meta,
        val data: HashMap<String, GamePrice>,
    )
}
