package saschpe.gameon.mobile.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.data.core.model.GamePrice
import saschpe.gameon.domain.Model.getGameInfoUseCase
import saschpe.gameon.domain.Model.getGameOverviewUseCase
import saschpe.gameon.domain.Model.getGamePricesUseCase

class GameViewModel : ViewModel() {
    val gameInfoLiveData = MutableLiveData<GameInfo>()
    val gameOverviewLiveData = MutableLiveData<GameOverview>()
    val gamePriceLiveData = MutableLiveData<GamePrice>()

    fun getGameInfo(plain: String) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getGameInfoUseCase(plain)) {
            is Result.Success<HashMap<String, GameInfo>> -> launch(Dispatchers.Main) {
                gameInfoLiveData.value = result.data[plain]
            }
            is Result.Error -> throw result.throwable
        }
    }

    fun getGameOverview(plain: String) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getGameOverviewUseCase(plain)) {
            is Result.Success<HashMap<String, GameOverview>> -> launch(Dispatchers.Main) {
                gameOverviewLiveData.value = result.data[plain]
            }
            is Result.Error -> throw result.throwable
        }
    }

    fun getGamePrice(plain: String) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getGamePricesUseCase(plain)) {
            is Result.Success<HashMap<String, GamePrice>> -> launch(Dispatchers.Main) {
                gamePriceLiveData.value = result.data[plain]
            }
            is Result.Error -> throw result.throwable
        }
    }
}