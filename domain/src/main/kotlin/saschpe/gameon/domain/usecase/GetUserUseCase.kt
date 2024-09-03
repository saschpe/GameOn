package saschpe.gameon.domain.usecase

import com.google.firebase.auth.FirebaseUser
import saschpe.gameon.data.remote.firebase.repository.UserRepository
import saschpe.gameon.domain.UseCase

// TODO: Map to proper user domain model
class GetUserUseCase(
    private val userRepository: UserRepository,
) : UseCase<Void, FirebaseUser> {
    override suspend fun invoke(vararg arguments: Void) = userRepository.getUser()
}
