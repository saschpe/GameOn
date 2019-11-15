package saschpe.gameon.domain

import saschpe.gameon.data.core.Result

interface UseCase<in ArgumentType : Any, out ResultType : Any> {
    suspend operator fun invoke(vararg arguments: ArgumentType): Result<ResultType>
}
