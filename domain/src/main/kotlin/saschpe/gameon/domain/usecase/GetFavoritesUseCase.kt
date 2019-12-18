package saschpe.gameon.domain.usecase

import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.local.model.FavoriteEntity
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.domain.UseCase

class GetFavoritesUseCase(
    private val favoritesLocalRepository: FavoritesLocalRepository
) : UseCase<String, List<Favorite>> {
    override suspend fun invoke(vararg arguments: String): Result<List<Favorite>> {
        val result = if (arguments.isEmpty()) {
            favoritesLocalRepository.getAll()
        } else {
            favoritesLocalRepository.getAllByPlains(arguments.toList())
        }

        return when (result) {
            is Result.Success<List<FavoriteEntity>> -> {
                Result.Success(result.data.map { favoriteEntity ->
                    Favorite(
                        id = favoriteEntity.id,
                        createdAt = favoriteEntity.createdAt,
                        plain = favoriteEntity.plain,
                        priceThreshold = favoriteEntity.priceThreshold
                    )
                })
            }
            is Result.Error -> result
        }
    }
}
