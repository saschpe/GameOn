package saschpe.gameon.mobile.game.reviews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.domain.Module.getGameInfoUseCase
import saschpe.gameon.domain.Module.getSteamReviewUrlUseCase

class GameOtherViewModel : ViewModel() {
    val gameInfoLiveData = MutableLiveData<Result<GameInfo>>()

    fun getGameInfo(plain: String) = viewModelScope.launch {
        gameInfoLiveData.value = when (val result = getGameInfoUseCase(plain)) {
            is Result.Success<HashMap<String, GameInfo>> -> Result.Success(result.data[plain]!!)
            is Result.Error -> result
        }
    }

    suspend fun getStreamReviewsUrl(plain: String) =
        when (val result = getSteamReviewUrlUseCase(plain)) {
            is Result.Success<String> -> result.data
            is Result.Error -> null
        }
}