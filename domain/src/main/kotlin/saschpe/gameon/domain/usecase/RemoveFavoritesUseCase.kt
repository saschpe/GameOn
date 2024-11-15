package saschpe.gameon.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.domain.UseCase

class RemoveFavoritesUseCase(private val favoritesLocalRepository: FavoritesLocalRepository) : UseCase<String, Unit> {
    /**
     * [arguments] List of plains
     */
    override suspend fun invoke(vararg arguments: String): Result<Unit> {
        require(arguments.isNotEmpty())
        val exceptions = mutableListOf<Throwable>()

        arguments.forEach { plain ->
            when (
                val result = withContext(Dispatchers.IO) {
                    favoritesLocalRepository.deleteByPlain(plain)
                }
            ) {
                is Result.Success<Unit> -> Unit
                is Result.Error -> exceptions.add(result.throwable)
            }
        }

        return if (exceptions.isEmpty()) {
            Result.Success(Unit)
        } else {
            Result.Error.withCause("${exceptions.size} exception(s) occurred", exceptions.first())
        }
    }
}
