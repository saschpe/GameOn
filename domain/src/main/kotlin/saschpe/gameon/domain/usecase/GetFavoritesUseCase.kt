package saschpe.gameon.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.local.model.FavoriteEntity
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.domain.UseCase
import saschpe.gameon.domain.mapper.toFavorite

class GetFavoritesUseCase(
    private val favoritesLocalRepository: FavoritesLocalRepository
) : UseCase<String, List<Favorite>> {
    override suspend fun invoke(vararg arguments: String): Result<List<Favorite>> {
        val result = withContext(Dispatchers.IO) {
            if (arguments.isEmpty()) {
                favoritesLocalRepository.getAll()
            } else {
                favoritesLocalRepository.getAllByPlains(arguments.toList())
            }
        }

        return when (result) {
            is Result.Success<List<FavoriteEntity>> -> Result.Success(result.data.map { it.toFavorite() })
            is Result.Error -> result
        }
    }
}
