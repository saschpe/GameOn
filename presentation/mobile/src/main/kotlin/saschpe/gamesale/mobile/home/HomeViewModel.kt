package saschpe.gamesale.mobile.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.Offer
import saschpe.gamesale.domain.Model.getDealsUseCase
import saschpe.gamesale.domain.Model.searchUseCase

class HomeViewModel : ViewModel() {
    private var currentDealsJob: Job? = null
    private var currentSearchJob: Job? = null

    val dealLiveData = MutableLiveData<List<Offer>>()
    val offerLiveData = MutableLiveData<List<Offer>>()

    fun getDeals() {
        currentDealsJob?.cancel() // Only have one, i.e. the latest, running...
        currentDealsJob = viewModelScope.launch(Dispatchers.IO) {
            when (val dealResult = getDealsUseCase()) {
                is Result.Success<List<Offer>> -> {

                }
                is Result.Error -> TODO("Implement")
            }
        }
    }

    fun search(query: String) {
        currentSearchJob?.cancel() // Only have one, i.e. the latest, running...
        currentSearchJob = viewModelScope.launch(Dispatchers.IO) {
            when (val searchResult = searchUseCase(query)) {
                is Result.Success<List<Offer>> -> {
                    launch(Dispatchers.Main) {
                        offerLiveData.value = searchResult.data.sortedBy { it.plain }
                    }
                }
                is Result.Error -> TODO("Implement")
            }
        }
    }
}