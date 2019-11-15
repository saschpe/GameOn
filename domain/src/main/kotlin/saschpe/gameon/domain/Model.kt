package saschpe.gameon.domain

import saschpe.gameon.data.remote.Module.dealsRemoteRepository
import saschpe.gameon.data.remote.Module.gameRemoteRepository
import saschpe.gameon.data.remote.Module.searchRemoteRepository
import saschpe.gameon.domain.usecase.*

object Model {
    val getDealsUseCase = GetDealsUseCase(dealsRemoteRepository)
    val getGameInfoUseCase = GetGameInfoUseCase(gameRemoteRepository)
    val getGameOverviewUseCase = GetGameOverviewUseCase(gameRemoteRepository)
    val getGamePricesUseCase = GetGamePricesUseCase(gameRemoteRepository)
    val searchUseCase = SearchUseCase(searchRemoteRepository)
}
