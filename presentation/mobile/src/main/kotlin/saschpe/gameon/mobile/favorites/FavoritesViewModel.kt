package saschpe.gameon.mobile.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.domain.Module.getFavoritesUseCase

class FavoritesViewModel : ViewModel() {
    val favoritesLiveData = MutableLiveData<Result<List<Favorite>>>()

    fun getFavorites() = viewModelScope.launch {
        favoritesLiveData.value = getFavoritesUseCase()
    }
}