package saschpe.gameon.mobile.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.domain.Module.searchUseCase

class SearchViewModel : ViewModel() {
    val searchLiveData = MutableLiveData<Result<List<Offer>>>()

    fun search(query: String) = viewModelScope.launch {
        searchLiveData.value = searchUseCase(query)
    }
}