package saschpe.gameon.mobile.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.domain.Module.searchUseCase

class SearchViewModel : ViewModel() {
    private var currentSearchJob: Job? = null

    val searchLiveData = MutableLiveData<List<Offer>>()

    fun search(query: String) {
        currentSearchJob?.cancel() // Only have one, i.e. the latest, running...
        currentSearchJob = viewModelScope.launch {
            when (val result = searchUseCase(query)) {
                is Result.Success<List<Offer>> -> searchLiveData.value =
                    result.data.sortedBy { it.plain }
                is Result.Error -> throw result.throwable
            }
        }
    }
}