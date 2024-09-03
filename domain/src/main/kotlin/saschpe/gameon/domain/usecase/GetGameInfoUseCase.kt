package saschpe.gameon.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.remote.itad.repository.GameRemoteRepository
import saschpe.gameon.domain.UseCase

class GetGameInfoUseCase(
    private val gameRemoteRepository: GameRemoteRepository,
) : UseCase<String, HashMap<String, GameInfo>> {
    override suspend fun invoke(vararg arguments: String): Result<HashMap<String, GameInfo>> {
        // TODO: Persistence, check local cache first...

        return when (val result = withContext(Dispatchers.IO) {
            gameRemoteRepository.info(arguments.toList())
        }) {
            is Result.Success<GameRemoteRepository.GameInfoResponse> -> {
                val gameInfos = result.data.data
                if (gameInfos.isEmpty()) {
                    Result.Error.withMessage("No game information objects for search '$arguments'")
                } else {
                    Result.Success(gameInfos)
                }
            }
            is Result.Error -> return result
        }
    }
}
