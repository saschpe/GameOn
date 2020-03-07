package saschpe.gameon.domain

import saschpe.gameon.data.local.Module.favoritesLocalRepository
import saschpe.gameon.data.remote.itad.Module.dealsRemoteRepository
import saschpe.gameon.data.remote.itad.Module.gameRemoteRepository
import saschpe.gameon.data.remote.itad.Module.searchRemoteRepository
import saschpe.gameon.domain.usecase.*

object Module {
    val addFavoritesUseCase = AddFavoritesUseCase(favoritesLocalRepository)
    val getDealsUseCase = GetDealsUseCase(dealsRemoteRepository)
    val getGameInfoUseCase = GetGameInfoUseCase(gameRemoteRepository)
    val getGameOverviewUseCase = GetGameOverviewUseCase(gameRemoteRepository)
    val getGamePricesUseCase = GetGamePricesUseCase(gameRemoteRepository)
    val getFavoritesUseCase = GetFavoritesUseCase(favoritesLocalRepository)
    val getPriceAlertsUseCase =
        GetPriceAlertsUseCase(favoritesLocalRepository, gameRemoteRepository)
    val getFavoriteUseCase = GetFavoriteUseCase(favoritesLocalRepository)
    val updateFavoritesUseCase = UpdateFavoritesUseCase(favoritesLocalRepository)
    val removeFavoritesUseCase = RemoveFavoritesUseCase(favoritesLocalRepository)
    val searchUseCase = SearchUseCase(searchRemoteRepository)
}
