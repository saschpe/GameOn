package saschpe.gameon.domain.usecase

import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.local.model.FavoriteEntity
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.domain.UseCase

class GetFavoriteUseCase(
    private val favoritesLocalRepository: FavoritesLocalRepository
) : UseCase<String, Favorite> {
    override suspend fun invoke(vararg arguments: String): Result<Favorite> {
        require(arguments.size == 1)

        return when (val result = favoritesLocalRepository.getByPlain(arguments.first())) {
            is Result.Success<FavoriteEntity> -> {
                Result.Success(
                    Favorite(
                        createdAt = result.data.createdAt,
                        plain = result.data.plain,
                        priceThreshold = result.data.priceThreshold
                    )
                )
            }
            is Result.Error -> result
        }
    }
}
