package saschpe.gameon.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.local.model.FavoriteEntity
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.data.remote.firebase.repository.FavoritesRemoteRepository
import saschpe.gameon.data.remote.firebase.repository.UserRepository
import saschpe.gameon.domain.UseCase
import saschpe.gameon.domain.mapper.toFavorite

class GetFavoriteUseCase(
    private val favoritesLocalRepository: FavoritesLocalRepository,
    private val favoritesRemoteRepository: FavoritesRemoteRepository,
    private val userRepository: UserRepository
) : UseCase<String, Favorite> {
    override suspend fun invoke(vararg arguments: String): Result<Favorite> {
        require(arguments.size == 1)

        return when (val result = withContext(Dispatchers.IO) {
            favoritesLocalRepository.getByPlain(arguments.first())
        }) {
            is Result.Success<FavoriteEntity> -> Result.Success(result.data.toFavorite())
            is Result.Error -> result
        }
    }
}
