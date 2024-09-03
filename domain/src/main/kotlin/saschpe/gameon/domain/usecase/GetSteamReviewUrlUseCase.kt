package saschpe.gameon.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.remote.itad.repository.GameRemoteRepository
import saschpe.gameon.domain.UseCase

class GetSteamReviewUrlUseCase(
    private val gameRemoteRepository: GameRemoteRepository,
) : UseCase<String, String> {
    override suspend fun invoke(vararg arguments: String): Result<String> {
        require(arguments.size == 1)
        return when (val result = withContext(Dispatchers.IO) {
            gameRemoteRepository.prices(plains = arguments.toList(), shops = listOf("steam"))
        }) {
            is Result.Success<GameRemoteRepository.GamePricesResponse> -> when {
                result.data.data.isEmpty() -> Result.Error.withMessage("No games overviews for search '$arguments'")
                else -> {
                    val steamUrl = result.data.data[arguments[0]]?.list?.get(0)?.url
                    if (steamUrl?.isNotEmpty() == true) {
                        Result.Success("$steamUrl#app_reviews_hash")
                    } else {
                        Result.Error.withMessage("Unable to retrieve Steam URL")
                    }
                }
            }
            is Result.Error -> result
        }
    }
}
