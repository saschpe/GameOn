package saschpe.gamesale.data.remote.repository

import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable
import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.remote.Api

class GameRemoteRepository(
    private val api: Api
) : RemoteRepository {
    suspend fun info(plains: List<String>): Result<GameInfoResponse> = asResult {
        api.get<GameInfoResponse>("game/info") {
            parameter("plains", plains.joinToString(separator = ","))
        }
    }

    @Serializable
    data class GameInfoResponse(
        val data: List<Game>
    ) {
        @Serializable
        data class Game(
            val title: String,
            val image: String,
            val is_dlc: Boolean,
            val urls: Urls
        ) {
            @Serializable
            data class Urls(
                val game: String
            )
        }
    }
}