package saschpe.gamesale.data.remote.repository

import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.GameInfo
import saschpe.gamesale.data.core.model.GameOverview
import saschpe.gamesale.data.remote.Api

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
     * Get game price overview.
     */
    suspend fun overview(
        plains: List<String>,
        region: String = "eu1",
        country: String = "de",
        shops: List<String> = listOf("steam"),
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

}