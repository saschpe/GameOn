package saschpe.gameon.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.data.remote.itad.repository.DealsRemoteRepository
import saschpe.gameon.domain.UseCase

class GetDealsUseCase(
    private val dealsRemoteRepository: DealsRemoteRepository
) : UseCase<String, List<Offer>> {
    override suspend fun invoke(vararg arguments: String): Result<List<Offer>> {
        // TODO: Pass down region, country, etc.

        return when (val result = withContext(Dispatchers.IO) {
            dealsRemoteRepository.list()
        }) {
            is Result.Success<DealsRemoteRepository.DealResponse> -> {
                val dealResponses = result.data.data.offers
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
