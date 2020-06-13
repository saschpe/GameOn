package saschpe.gameon.domain

import saschpe.gameon.data.local.Module.favoritesLocalRepository
import saschpe.gameon.data.remote.firebase.Module.userRepository
import saschpe.gameon.data.remote.itad.Module.dealsRemoteRepository
import saschpe.gameon.data.remote.itad.Module.gameRemoteRepository
import saschpe.gameon.data.remote.itad.Module.searchRemoteRepository
import saschpe.gameon.domain.usecase.*

object Module {
    val addFavoritesUseCase = AddFavoritesUseCase(favoritesLocalRepository)
    val dismissPriceAlertUseCase = DismissPriceAlertUseCase(favoritesLocalRepository)
    val getDealsUseCase = GetDealsUseCase(dealsRemoteRepository)
    val getGameInfoUseCase = GetGameInfoUseCase(gameRemoteRepository)
    val getGameOverviewUseCase = GetGameOverviewUseCase(gameRemoteRepository)
    val getGamePricesUseCase = GetGamePricesUseCase(gameRemoteRepository)
    val getFavoritesUseCase = GetFavoritesUseCase(favoritesLocalRepository)
    val getFavoriteUseCase = GetFavoriteUseCase(favoritesLocalRepository)
    val getPriceAlertsUseCase = GetPriceAlertsUseCase(favoritesLocalRepository, gameRemoteRepository)
    val getSteamReviewUrlUseCase = GetSteamReviewUrlUseCase(gameRemoteRepository)
    val getUserUseCase = GetUserUseCase(userRepository)
    val removeFavoritesUseCase = RemoveFavoritesUseCase(favoritesLocalRepository)
    val searchUseCase = SearchUseCase(searchRemoteRepository)
    val signInWithEmailUseCase = SignInWithEmailUseCase(userRepository)
    val signInWithGoogleUseCase = SignInWithGoogleUseCase(userRepository)
    val signOutUseCase = SignOutUseCase(userRepository)
    val signUpWithEmailUseCase = SignUpWithEmailUseCase(userRepository)
    val updateFavoritesUseCase = UpdateFavoritesUseCase(favoritesLocalRepository)
}
