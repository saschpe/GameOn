package saschpe.gameon.domain

import saschpe.gameon.data.local.Module.favoritesLocalRepository
import saschpe.gameon.data.remote.firebase.Module.favoritesRemoteRepository
import saschpe.gameon.data.remote.firebase.Module.userRepository
import saschpe.gameon.data.remote.itad.Module.dealsRemoteRepository
import saschpe.gameon.data.remote.itad.Module.gameRemoteRepository
import saschpe.gameon.data.remote.itad.Module.searchRemoteRepository
import saschpe.gameon.domain.usecase.*

object Module {
    val addFavoritesUseCase = AddFavoritesUseCase(favoritesLocalRepository, favoritesRemoteRepository, userRepository)
    val dismissPriceAlertUseCase = DismissPriceAlertUseCase(favoritesLocalRepository)
    val getDealsUseCase = GetDealsUseCase(dealsRemoteRepository)
    val getGameInfoUseCase = GetGameInfoUseCase(gameRemoteRepository)
    val getGameOverviewUseCase = GetGameOverviewUseCase(gameRemoteRepository)
    val getGamePricesUseCase = GetGamePricesUseCase(gameRemoteRepository)
    val getFavoriteUseCase = GetFavoriteUseCase(favoritesLocalRepository, favoritesRemoteRepository, userRepository)
    val getFavoritesUseCase = GetFavoritesUseCase(favoritesLocalRepository, favoritesRemoteRepository, userRepository)
    val getPriceAlertsUseCase = GetPriceAlertsUseCase(favoritesLocalRepository, gameRemoteRepository)
    val getUserUseCase = GetUserUseCase(userRepository)
    val removeFavoritesUseCase =
        RemoveFavoritesUseCase(favoritesLocalRepository, favoritesRemoteRepository, userRepository)
    val searchUseCase = SearchUseCase(searchRemoteRepository)
    val signInWithEmailUseCase = SignInWithEmailUseCase(userRepository)
    val signInWithGoogleUseCase = SignInWithGoogleUseCase(userRepository)
    val signOutUseCase = SignOutUseCase(userRepository)
    val signUpWithEmailUseCase = SignUpWithEmailUseCase(userRepository)
    val updateFavoritesUseCase =
        UpdateFavoritesUseCase(favoritesLocalRepository, favoritesRemoteRepository, userRepository)
}
