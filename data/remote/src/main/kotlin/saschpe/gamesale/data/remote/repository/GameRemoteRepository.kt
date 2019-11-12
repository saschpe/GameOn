package saschpe.gamesale.data.remote.repository

import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable
import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.Game
import saschpe.gamesale.data.remote.Api

class GameRemoteRepository(
    private val api: Api
) : RemoteRepository {
    suspend fun info(slugs: List<String>): Result<GameInfoResponse> = asResult {
        api.get<GameInfoResponse>("game/info") {
            parameter("plains", slugs.joinToString(separator = ","))
        }
    }

    @Serializable
    data class GameInfoResponse(
        val data: List<Game>
    ) {
    }

}