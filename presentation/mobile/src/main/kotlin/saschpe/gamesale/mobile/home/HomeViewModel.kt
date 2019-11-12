package saschpe.gamesale.mobile.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.Game
import saschpe.gamesale.data.core.model.Offer
import saschpe.gamesale.domain.Model.getDealsUseCase
import saschpe.gamesale.domain.Model.getGameInfoUseCase

class HomeViewModel : ViewModel() {
    private var currentDealsJob: Job? = null

    val dealLiveData = MutableLiveData<List<Offer>>()

    fun getDeals() {
        currentDealsJob?.cancel() // Only have one, i.e. the latest, running...
        currentDealsJob = viewModelScope.launch(Dispatchers.IO) {
            when (val dealResult = getDealsUseCase()) {
                is Result.Success<List<Offer>> -> {
                    launch(Dispatchers.Main) {
                        dealLiveData.value = dealResult.data.sortedBy { it.added }
                    }
                }
                is Result.Error -> TODO("Implement")
            }
        }
    }

    /*fun getGameInfo(): Result<List<Game>> {
        viewModelScope.launch(Dispatchers.IO) {
            getGameInfoUseCase()
        }
    }*/
}