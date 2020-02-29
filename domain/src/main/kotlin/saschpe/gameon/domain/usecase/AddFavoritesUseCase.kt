package saschpe.gameon.domain.usecase

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.data.remote.firebase.repository.FavoritesRemoteRepository
import saschpe.gameon.data.remote.firebase.repository.UserRepository
import saschpe.gameon.domain.UseCase
import saschpe.gameon.domain.mapper.toFavoriteEntity

class AddFavoritesUseCase(
    private val favoritesLocalRepository: FavoritesLocalRepository,
    private val favoritesRemoteRepository: FavoritesRemoteRepository,
    private val userRepository: UserRepository
) : UseCase<Favorite, Unit> {
    override suspend fun invoke(vararg arguments: Favorite): Result<Unit> {
        require(arguments.isNotEmpty())
        val exceptions = mutableListOf<Throwable>()

        arguments.forEach { favorite ->
            when (val localResult = withContext(Dispatchers.IO) {
                favoritesLocalRepository.insert(favorite.toFavoriteEntity())
            }) {
                is Result.Success<Unit> -> userWhenSignedIn()?.let { user ->
                    when (val remoteResult = favoritesRemoteRepository
                        .addOrUpdateFavorite(user.uid, favorite)) {
                        is Result.Success<Void> -> Unit
                        is Result.Error -> exceptions.add(remoteResult.throwable)
                    }
                }
                is Result.Error -> exceptions.add(localResult.throwable)
            }
        }

        return if (exceptions.isEmpty()) {
            Result.Success(Unit)
        } else {
            Result.Error.withCause("${exceptions.size} exception(s) occurred", exceptions.first())
        }
    }

    private suspend fun userWhenSignedIn() = when (val result = userRepository.getUser()) {
        is Result.Success<FirebaseUser> -> result.data
        else -> null
    }
}
