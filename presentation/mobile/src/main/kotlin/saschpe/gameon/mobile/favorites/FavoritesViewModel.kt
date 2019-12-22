package saschpe.gameon.mobile.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.domain.Module.getFavoritesUseCase

class FavoritesViewModel : ViewModel() {
    val favoritesLiveData = MutableLiveData<List<Favorite>>()

    fun getFavorites() = viewModelScope.launch {
        when (val result = getFavoritesUseCase()) {
            is Result.Success<List<Favorite>> -> favoritesLiveData.value = result.data
            is Result.Error -> throw result.throwable
        }
    }
}