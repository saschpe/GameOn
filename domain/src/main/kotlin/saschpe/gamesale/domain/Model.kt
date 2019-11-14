package saschpe.gamesale.domain

import saschpe.gamesale.data.remote.Module.dealsRemoteRepository
import saschpe.gamesale.data.remote.Module.gameRemoteRepository
import saschpe.gamesale.data.remote.Module.searchRemoteRepository
import saschpe.gamesale.domain.usecase.*

object Model {
    val getDealsUseCase = GetDealsUseCase(dealsRemoteRepository)
    val getGameInfoUseCase = GetGameInfoUseCase(gameRemoteRepository)
    val getGameOverviewUseCase = GetGameOverviewUseCase(gameRemoteRepository)
    val getGamePricesUseCase = GetGamePricesUseCase(gameRemoteRepository)
    val searchUseCase = SearchUseCase(searchRemoteRepository)
}