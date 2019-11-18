package saschpe.gameon.domain.usecase

import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.data.remote.repository.SearchRemoteRepository
import saschpe.gameon.domain.UseCase

class SearchUseCase(
    private val searchRemoteRepository: SearchRemoteRepository
) : UseCase<String, List<Offer>> {
    override suspend fun invoke(vararg arguments: String): Result<List<Offer>> {
        require(arguments.size == 1)
        // TODO: Pass down region, country, etc.

        return when (val result = searchRemoteRepository.search(arguments[0])) {
            is Result.Success<SearchRemoteRepository.SearchResponse> -> {
                val offerResponses = result.data.data.list
                if (offerResponses.isEmpty()) {
                    Result.Error.withMessage("No offers for search '$arguments'")
                } else {
                    Result.Success(offerResponses)
                }
            }
            is Result.Error -> result
        }
    }
}