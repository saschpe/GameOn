package saschpe.gameon.domain.usecase

import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Watch
import saschpe.gameon.data.local.model.WatchEntity
import saschpe.gameon.data.local.repository.WatchlistLocalRepository
import saschpe.gameon.domain.UseCase

class GetWatchesUseCase(
    private val watchlistLocalRepository: WatchlistLocalRepository
) : UseCase<Unit, List<Watch>> {
    override suspend fun invoke(vararg arguments: Unit): Result<List<Watch>> {
        require(arguments.isEmpty())

        return when (val result = watchlistLocalRepository.getAll()) {
            is Result.Success<List<WatchEntity>> -> {
                Result.Success(result.data.map { watchEntity ->
                    Watch(plain = watchEntity.plain)
                })
            }
            is Result.Error -> result
        }
    }
}
