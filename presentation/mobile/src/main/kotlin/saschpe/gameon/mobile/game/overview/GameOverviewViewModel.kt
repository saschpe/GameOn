package saschpe.gameon.mobile.game.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.domain.Module.addFavoritesUseCase
import saschpe.gameon.domain.Module.getFavoriteUseCase
import saschpe.gameon.domain.Module.getGameInfoUseCase
import saschpe.gameon.domain.Module.getGameOverviewUseCase
import saschpe.gameon.domain.Module.removeFavoritesUseCase

class GameOverviewViewModel : ViewModel() {
    val gameInfoLiveData = MutableLiveData<GameInfo>()
    val gameOverviewLiveData = MutableLiveData<GameOverview>()
    val favoriteLiveData = MutableLiveData<Favorite?>()

    fun getGameInfo(plain: String) = viewModelScope.launch {
        when (val result = getGameInfoUseCase(plain)) {
            is Result.Success<HashMap<String, GameInfo>> -> gameInfoLiveData.value =
                result.data[plain]
            is Result.Error -> throw result.throwable
        }
    }

    fun getGameOverview(plain: String) = viewModelScope.launch {
        when (val result = getGameOverviewUseCase(plain)) {
            is Result.Success<HashMap<String, GameOverview>> -> gameOverviewLiveData.value =
                result.data[plain]
            is Result.Error -> throw result.throwable
        }
    }

    fun getFavorite(plain: String) = viewModelScope.launch {
        when (val result = getFavoriteUseCase(plain)) {
            is Result.Success<Favorite> -> favoriteLiveData.value = result.data
            is Result.Error -> favoriteLiveData.value = null
        }
    }

    fun addFavorite(plain: String, priceThreshold: Long? = null) = viewModelScope.launch {
        val favorite = Favorite(plain = plain, priceThreshold = priceThreshold)

        when (val result = addFavoritesUseCase(favorite)) {
            is Result.Success<Unit> -> favoriteLiveData.value = favorite
            is Result.Error -> throw result.throwable
        }
    }

    fun removeFavorite(plain: String) = viewModelScope.launch {
        when (val result = removeFavoritesUseCase(plain)) {
            is Result.Success<Unit> -> favoriteLiveData.value = null
            is Result.Error -> throw result.throwable
        }
    }
}