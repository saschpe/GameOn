package saschpe.gameon.mobile.game.reviews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.domain.Module.getGameInfoUseCase

class GameReviewsViewModel : ViewModel() {
    val gameInfoLiveData = MutableLiveData<GameInfo>()

    fun getGameInfo(plain: String) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getGameInfoUseCase(plain)) {
            is Result.Success<HashMap<String, GameInfo>> -> launch(Dispatchers.Main) {
                gameInfoLiveData.value = result.data[plain]
            }
            is Result.Error -> throw result.throwable
        }
    }
}