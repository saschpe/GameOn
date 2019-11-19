package saschpe.gameon.domain.usecase

import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Watch
import saschpe.gameon.data.local.model.WatchEntity
import saschpe.gameon.data.local.repository.WatchlistLocalRepository
import saschpe.gameon.domain.UseCase

class AddWatchesUseCase(
    private val watchlistLocalRepository: WatchlistLocalRepository
) : UseCase<Watch, Unit> {
    override suspend fun invoke(vararg arguments: Watch): Result<Unit> {
        require(arguments.isNotEmpty())
        val exceptions = mutableListOf<Throwable>()

        arguments.forEach { watch ->
            when (val result = watchlistLocalRepository.insert(
                WatchEntity(
                    createdAt = watch.createdAt,
                    plain = watch.plain,
                    title = watch.title,
                    priceThreshold = watch.priceThreshold
                )
            )) {
                is Result.Success<Unit> -> Unit
                is Result.Error -> exceptions.add(result.throwable)
            }
        }

        return if (exceptions.isEmpty()) {
            Result.Success(Unit)
        } else {
            Result.Error.withCause("${exceptions.size} exception(s) occurred", exceptions.first())
        }
    }
}
