package saschpe.gameon.domain

import saschpe.gameon.data.local.Module.watchlistLocalRepository
import saschpe.gameon.data.remote.Module.dealsRemoteRepository
import saschpe.gameon.data.remote.Module.gameRemoteRepository
import saschpe.gameon.data.remote.Module.searchRemoteRepository
import saschpe.gameon.domain.usecase.*

object Module {
    val addWatchesUseCase = AddWatchesUseCase(watchlistLocalRepository)
    val getDealsUseCase = GetDealsUseCase(dealsRemoteRepository)
    val getGameInfoUseCase = GetGameInfoUseCase(gameRemoteRepository)
    val getGameOverviewUseCase = GetGameOverviewUseCase(gameRemoteRepository)
    val getGamePricesUseCase = GetGamePricesUseCase(gameRemoteRepository)
    val getWatchesUseCase = GetWatchesUseCase(watchlistLocalRepository)
    val removeWatchesUseCase = RemoveWatchesUseCase(watchlistLocalRepository)
    val searchUseCase = SearchUseCase(searchRemoteRepository)
}
