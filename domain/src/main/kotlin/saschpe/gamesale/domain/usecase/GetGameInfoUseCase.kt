package saschpe.gamesale.domain.usecase

import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.GameInfo
import saschpe.gamesale.data.remote.repository.GameRemoteRepository
import saschpe.gamesale.domain.UseCase

class GetGameInfoUseCase(
    private val gameRemoteRepository: GameRemoteRepository
) : UseCase<String, HashMap<String, GameInfo>> {
    override suspend fun invoke(vararg arguments: String): Result<HashMap<String, GameInfo>> {
        // TODO: Persistence, check local cache first...

        return when (val result = gameRemoteRepository.info(arguments.toList())) {
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