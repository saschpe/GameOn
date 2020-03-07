package saschpe.gameon.mobile.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.domain.Module.getUserUseCase
import saschpe.gameon.domain.Module.signOutUseCase

class ProfileViewModel : ViewModel() {
    val userLiveData = MutableLiveData<Result<FirebaseUser>>()

    fun getUser() = viewModelScope.launch { userLiveData.value = getUserUseCase() }

    fun signOut() = viewModelScope.launch {
        signOutUseCase()
        userLiveData.value = getUserUseCase()
    }
}