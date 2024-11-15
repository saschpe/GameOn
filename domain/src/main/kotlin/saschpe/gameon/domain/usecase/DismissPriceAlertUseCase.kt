package saschpe.gameon.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.domain.UseCase

class DismissPriceAlertUseCase(private val favoritesLocalRepository: FavoritesLocalRepository) : UseCase<String, Unit> {
    /**
     * [arguments] Plain of favorite to remove
     */
    override suspend fun invoke(vararg arguments: String): Result<Unit> {
        require(arguments.size == 1)
        return withContext(Dispatchers.IO) {
            favoritesLocalRepository.dismissByPlain(arguments.first())
        }
    }
}
