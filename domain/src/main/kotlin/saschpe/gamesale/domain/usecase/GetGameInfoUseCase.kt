package saschpe.gamesale.domain.usecase

import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.Game
import saschpe.gamesale.data.remote.repository.GameRemoteRepository
import saschpe.gamesale.domain.UseCase

class GetGameInfoUseCase(
    private val gameRemoteRepository: GameRemoteRepository
) : UseCase<String, List<Game>> {
    override suspend fun invoke(vararg arguments: String): Result<List<Game>> {
        // TODO: Persistence, check local cache first...

        return when (val result = gameRemoteRepository.info(arguments.toList())) {
            is Result.Success<GameRemoteRepository.GameInfoResponse> -> {
                val games = result.data.data
                if (games.isEmpty()) {
                    Result.Error.withMessage("No games for search '$arguments'")
                } else {
                    Result.Success(games)
                }
            }
            is Result.Error -> return result
        }
    }
}