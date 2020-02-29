package saschpe.gameon.domain.usecase

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.data.remote.firebase.repository.FavoritesRemoteRepository
import saschpe.gameon.data.remote.firebase.repository.UserRepository
import saschpe.gameon.domain.UseCase

class RemoveFavoritesUseCase(
    private val favoritesLocalRepository: FavoritesLocalRepository,
    private val favoritesRemoteRepository: FavoritesRemoteRepository,
    private val userRepository: UserRepository
) : UseCase<String, Unit> {
    /**
     * [arguments] List of plains
     */
    override suspend fun invoke(vararg arguments: String): Result<Unit> {
        require(arguments.isNotEmpty())
        val exceptions = mutableListOf<Throwable>()

        arguments.forEach { plain ->
            when (val result = withContext(Dispatchers.IO) {
                favoritesLocalRepository.deleteByPlain(plain)
            }) {
                is Result.Success<Unit> -> userWhenSignedIn()?.let { user ->
                    when (val remoteResult =
                        favoritesRemoteRepository.removeFavorite(user.uid, plain)) {
                        is Result.Success<Void> -> Unit
                        is Result.Error -> exceptions.add(remoteResult.throwable)
                    }
                }
                is Result.Error -> exceptions.add(result.throwable)
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
