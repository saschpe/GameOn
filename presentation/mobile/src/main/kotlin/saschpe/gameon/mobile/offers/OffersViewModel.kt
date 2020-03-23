package saschpe.gameon.mobile.offers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.domain.Module.getDealsUseCase

class OffersViewModel : ViewModel() {
    val dealLiveData = MutableLiveData<Result<List<Offer>>>()

    fun getDeals() = viewModelScope.launch {
        dealLiveData.value = getDealsUseCase()
    }
}