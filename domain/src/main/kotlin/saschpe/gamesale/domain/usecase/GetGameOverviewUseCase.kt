package saschpe.gamesale.domain.usecase

import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.GameOverview
import saschpe.gamesale.data.remote.repository.GameRemoteRepository
import saschpe.gamesale.domain.UseCase

class GetGameOverviewUseCase(
    private val gameRemoteRepository: GameRemoteRepository
) : UseCase<String, HashMap<String, GameOverview>> {
    override suspend fun invoke(vararg arguments: String): Result<HashMap<String, GameOverview>> =
        when (val result = gameRemoteRepository.overview(arguments.toList())) {
            is Result.Success<GameRemoteRepository.GameOverviewResponse> -> {
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