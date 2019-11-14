package saschpe.gamesale.mobile.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.Offer
import saschpe.gamesale.domain.Model.getDealsUseCase

class HomeViewModel : ViewModel() {
    val dealLiveData = MutableLiveData<List<Offer>>()

    fun getDeals() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getDealsUseCase()) {
                is Result.Success<List<Offer>> -> launch(Dispatchers.Main) {
                    dealLiveData.value = result.data.sortedBy { it.added }
                }
                is Result.Error -> throw result.throwable
            }
        }
    }

    /*fun getGameInfo(): Result<List<GameInfo>> {
        viewModelScope.launch(Dispatchers.IO) {
            getGameInfoUseCase()
        }
    }*/
}