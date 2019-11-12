package saschpe.gamesale.domain

import saschpe.gamesale.data.remote.Module.dealsRemoteRepository
import saschpe.gamesale.data.remote.Module.gameRemoteRepository
import saschpe.gamesale.data.remote.Module.searchRemoteRepository
import saschpe.gamesale.domain.usecase.GetDealsUseCase
import saschpe.gamesale.domain.usecase.GetGameInfoUseCase
import saschpe.gamesale.domain.usecase.SearchUseCase

object Model {
    val getDealsUseCase = GetDealsUseCase(dealsRemoteRepository)
    val getGameInfoUseCase = GetGameInfoUseCase(gameRemoteRepository)
    val searchUseCase = SearchUseCase(searchRemoteRepository)
}