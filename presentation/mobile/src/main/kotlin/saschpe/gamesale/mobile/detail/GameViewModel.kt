package saschpe.gamesale.mobile.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.GameInfo
import saschpe.gamesale.data.core.model.GameOverview
import saschpe.gamesale.domain.Model.getGameInfoUseCase
import saschpe.gamesale.domain.Model.getGameOverviewUseCase

class GameViewModel : ViewModel() {
    val gameInfoLiveData = MutableLiveData<GameInfo>()
    val gameOverviewLiveData = MutableLiveData<GameOverview>()

    fun getGameInfo(plain: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getGameInfoUseCase(plain)) {
                is Result.Success<HashMap<String, GameInfo>> -> launch(Dispatchers.Main) {
                    gameInfoLiveData.value = result.data[plain]
                }
                is Result.Error -> throw result.throwable
            }
        }
    }

    fun getGameOverview(plain: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getGameOverviewUseCase(plain)) {
                is Result.Success<HashMap<String, GameOverview>> -> launch(Dispatchers.Main) {
                    gameOverviewLiveData.value = result.data[plain]
                }
                is Result.Error -> throw result.throwable
            }
        }
    }
}