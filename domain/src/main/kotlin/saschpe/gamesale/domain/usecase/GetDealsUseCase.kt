package saschpe.gamesale.domain.usecase

import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.remote.repository.DealsRemoteRepository
import saschpe.gamesale.data.core.model.Offer
import saschpe.gamesale.domain.UseCase

class GetDealsUseCase(
    private val dealsRemoteRepository: DealsRemoteRepository
) : UseCase<String, List<Offer>> {
    override suspend fun invoke(vararg arguments: String): Result<List<Offer>> {
        // TODO: Pass down region, country, etc.

        return when (val result = dealsRemoteRepository.list()) {
            is Result.Success<DealsRemoteRepository.DealResponse> -> {
                val dealResponses = result.data.data.list
                if (dealResponses.isEmpty()) {
                    Result.Error.withMessage("No deals for search '$arguments'")
                } else {
                    Result.Success(dealResponses)
                }
            }
            is Result.Error -> result
        }
    }
}