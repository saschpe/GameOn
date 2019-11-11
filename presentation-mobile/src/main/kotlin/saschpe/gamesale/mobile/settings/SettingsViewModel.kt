package saschpe.gamesale.mobile.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.Preference

class SettingsViewModel : ViewModel() {
    val selectedPreference = MutableLiveData<Preference>()
}
