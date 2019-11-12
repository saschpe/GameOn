package saschpe.gamesale.mobile.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gamesale.data.core.Result
import saschpe.gamesale.data.core.model.Offer
import saschpe.gamesale.domain.Model.searchUseCase

class SearchViewModel : ViewModel() {
    private var currentSearchJob: Job? = null

    val searchLiveData = MutableLiveData<List<Offer>>()

    fun search(query: String) {
        currentSearchJob?.cancel() // Only have one, i.e. the latest, running...
        currentSearchJob = viewModelScope.launch(Dispatchers.IO) {
            when (val searchResult = searchUseCase(query)) {
                is Result.Success<List<Offer>> -> {
                    launch(Dispatchers.Main) {
                        searchLiveData.value = searchResult.data.sortedBy { it.plain }
                    }
                }
                is Result.Error -> TODO("Implement")
            }
        }
    }
}