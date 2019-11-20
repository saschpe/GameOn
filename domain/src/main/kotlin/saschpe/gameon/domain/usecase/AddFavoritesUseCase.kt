package saschpe.gameon.domain.usecase

import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.local.model.FavoriteEntity
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.domain.UseCase

class AddFavoritesUseCase(
    private val favoritesLocalRepository: FavoritesLocalRepository
) : UseCase<Favorite, Unit> {
    override suspend fun invoke(vararg arguments: Favorite): Result<Unit> {
        require(arguments.isNotEmpty())
        val exceptions = mutableListOf<Throwable>()

        arguments.forEach { favorite ->
            when (val result = favoritesLocalRepository.insert(
                FavoriteEntity(
                    createdAt = favorite.createdAt,
                    plain = favorite.plain,
                    title = favorite.title,
                    priceThreshold = favorite.priceThreshold
                )
            )) {
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