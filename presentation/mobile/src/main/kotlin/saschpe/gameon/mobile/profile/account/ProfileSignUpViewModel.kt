package saschpe.gameon.mobile.profile.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.domain.Module.signUpWithEmailUseCase

class ProfileSignUpViewModel : ViewModel() {
    val signUpLiveData = MutableLiveData<Result<AuthResult>>()

    fun signUpWithEmail(email: String, password: String) = viewModelScope.launch {
        signUpLiveData.value = signUpWithEmailUseCase(email, password)
    }
}