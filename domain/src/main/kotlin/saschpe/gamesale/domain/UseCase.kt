package saschpe.gamesale.domain

import saschpe.gamesale.data.core.Result

interface UseCase<in ArgumentType : Any, out ResultType : Any> {
    suspend operator fun invoke(vararg arguments: ArgumentType): Result<ResultType>
}
