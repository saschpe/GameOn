package saschpe.gameon.domain.usecase

import com.google.firebase.auth.AuthResult
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.remote.firebase.repository.UserRepository
import saschpe.gameon.domain.UseCase

class SignInWithEmailUseCase(
    private val userRepository: UserRepository,
) : UseCase<String, AuthResult> {
    override suspend fun invoke(vararg arguments: String): Result<AuthResult> {
        require(arguments.size == 2)
        return userRepository.signInWithEmail(arguments[0], arguments[1])
    }
}
