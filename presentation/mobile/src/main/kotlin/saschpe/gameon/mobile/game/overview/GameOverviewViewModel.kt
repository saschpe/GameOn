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
import saschpe.gameon.domain.Module.updateFavoritesUseCase

class GameOverviewViewModel : ViewModel() {
    val gameInfoLiveData = MutableLiveData<Result<GameInfo>>()
    val gameOverviewLiveData = MutableLiveData<Result<GameOverview>>()
    val favoriteLiveData = MutableLiveData<Result<Favorite>>()

    fun getGameInfo(plain: String) = viewModelScope.launch {
        gameInfoLiveData.value = when (val result = getGameInfoUseCase(plain)) {
            is Result.Success<HashMap<String, GameInfo>> -> Result.Success(result.data[plain]!!)
            is Result.Error -> result
        }
    }

    fun getGameOverview(plain: String) = viewModelScope.launch {
        gameOverviewLiveData.value = when (val result = getGameOverviewUseCase(plain)) {
            is Result.Success<HashMap<String, GameOverview>> -> Result.Success(result.data[plain]!!)
            is Result.Error -> result
        }
    }

    fun getFavorite(plain: String) = viewModelScope.launch {
        favoriteLiveData.value = getFavoriteUseCase(plain)
    }

    fun addFavorite(plain: String) = viewModelScope.launch {
        addFavoritesUseCase(Favorite(plain = plain))
        getFavorite(plain)
    }

    fun updateFavorite(favorite: Favorite) = viewModelScope.launch {
        updateFavoritesUseCase(favorite)
        getFavorite(favorite.plain)
    }

    fun removeFavorite(plain: String) = viewModelScope.launch {
        removeFavoritesUseCase(plain)
        getFavorite(plain)
    }
}
