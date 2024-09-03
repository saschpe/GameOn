package saschpe.gameon.domain.usecase

import saschpe.gameon.data.core.Result
import saschpe.gameon.data.remote.firebase.repository.UserRepository
import saschpe.gameon.domain.UseCase

class SignOutUseCase(
    private val userRepository: UserRepository,
) : UseCase<Void, Unit> {
    override suspend fun invoke(vararg arguments: Void): Result<Unit> = userRepository.signOut()
}
