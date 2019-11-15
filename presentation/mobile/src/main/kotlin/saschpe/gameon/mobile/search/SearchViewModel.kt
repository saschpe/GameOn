package saschpe.gameon.mobile.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.domain.Model.searchUseCase

class SearchViewModel : ViewModel() {
    private var currentSearchJob: Job? = null

    val searchLiveData = MutableLiveData<List<Offer>>()

    fun search(query: String) {
        currentSearchJob?.cancel() // Only have one, i.e. the latest, running...
        currentSearchJob = viewModelScope.launch(Dispatchers.IO) {
            when (val result = searchUseCase(query)) {
                is Result.Success<List<Offer>> -> launch(Dispatchers.Main) {
                    searchLiveData.value = result.data.sortedBy { it.plain }
                }
                is Result.Error -> throw result.throwable
            }
        }
    }
}