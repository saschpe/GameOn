package saschpe.gamesale.domain.usecase

import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.GamePrice
import saschpe.gamesale.data.remote.repository.GameRemoteRepository
import saschpe.gamesale.domain.UseCase

class GetGamePricesUseCase(
    private val gameRemoteRepository: GameRemoteRepository
) : UseCase<String, HashMap<String, GamePrice>> {
    override suspend fun invoke(vararg arguments: String): Result<HashMap<String, GamePrice>> =
        when (val result = gameRemoteRepository.prices(arguments.toList())) {
            is Result.Success<GameRemoteRepository.GamePricesResponse> -> {
                val gameOverviews = result.data.data
                if (gameOverviews.isEmpty()) {
                    Result.Error.withMessage("No games overviews for search '$arguments'")
                } else {
                    Result.Success(gameOverviews)
                }
            }
            is Result.Error -> result
        }
}