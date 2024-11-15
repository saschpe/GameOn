package saschpe.gameon.mobile.profile.account

import androidx.lifecycle.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import saschpe.gameon.data.core.Result
import saschpe.gameon.domain.Module.getUserUseCase
import saschpe.gameon.domain.Module.signInWithEmailUseCase
import saschpe.gameon.domain.Module.signInWithGoogleUseCase

class ProfileSignInViewModel : ViewModel() {
    val userLiveData: LiveData<Result<FirebaseUser>> =
        liveData(context = viewModelScope.coroutineContext) { emit(getUserUseCase()) }
    val signInLiveData = MutableLiveData<Result<AuthResult>>()

    fun signInWithEmail(email: String, password: String) = viewModelScope.launch {
        signInLiveData.value = signInWithEmailUseCase(email, password)
    }

    fun signInWithGoogle(googleIdToken: String) = viewModelScope.launch {
        signInLiveData.value = signInWithGoogleUseCase(googleIdToken)
    }
}
