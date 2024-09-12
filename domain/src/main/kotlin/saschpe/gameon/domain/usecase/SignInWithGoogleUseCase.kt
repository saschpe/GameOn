package saschpe.gameon.domain.usecase

import com.google.firebase.auth.AuthResult
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.remote.firebase.repository.UserRepository
import saschpe.gameon.domain.UseCase

class SignInWithGoogleUseCase(private val userRepository: UserRepository) : UseCase<String, AuthResult> {
    override suspend fun invoke(vararg arguments: String): Result<AuthResult> {
        require(arguments.size == 1)
        return userRepository.signInWithGoogle(arguments[0])
    }
}
