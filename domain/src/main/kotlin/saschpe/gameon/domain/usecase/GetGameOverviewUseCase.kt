package saschpe.gameon.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.data.remote.itad.repository.GameRemoteRepository
import saschpe.gameon.domain.UseCase

class GetGameOverviewUseCase(
    private val gameRemoteRepository: GameRemoteRepository,
) : UseCase<String, HashMap<String, GameOverview>> {
    override suspend fun invoke(vararg arguments: String) =
        when (val result = withContext(Dispatchers.IO) {
            gameRemoteRepository.overview(plains = arguments.toList())
        }) {
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
