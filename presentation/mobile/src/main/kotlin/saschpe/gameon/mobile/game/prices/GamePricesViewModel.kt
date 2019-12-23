package saschpe.gameon.mobile.game.prices

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GamePrice
import saschpe.gameon.domain.Module.getGamePricesUseCase

class GamePricesViewModel : ViewModel() {
    val gamePriceLiveData = MutableLiveData<GamePrice>()

    fun getGamePrice(plain: String) = viewModelScope.launch {
        when (val result = getGamePricesUseCase(plain)) {
            is Result.Success<HashMap<String, GamePrice>> -> gamePriceLiveData.value =
                result.data[plain]
            is Result.Error -> throw result.throwable
        }
    }
}