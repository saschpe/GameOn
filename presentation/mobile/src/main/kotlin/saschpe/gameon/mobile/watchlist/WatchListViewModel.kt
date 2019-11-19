package saschpe.gameon.mobile.watchlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Watch
import saschpe.gameon.domain.Module.getWatchesUseCase

class WatchListViewModel : ViewModel() {
    val watchesLiveData = MutableLiveData<List<Watch>>()

    fun getWatches() = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getWatchesUseCase()) {
            is Result.Success<List<Watch>> -> launch(Dispatchers.Main) {
                watchesLiveData.value = result.data
            }
            is Result.Error -> throw result.throwable
        }
    }

    fun addWatch() = viewModelScope.launch(Dispatchers.IO) {
        TODO()
    }

    fun removeWatch() = viewModelScope.launch(Dispatchers.IO) {
        TODO()
    }
}