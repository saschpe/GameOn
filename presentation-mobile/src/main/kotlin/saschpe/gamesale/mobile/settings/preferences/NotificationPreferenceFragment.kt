package saschpe.gamesale.mobile.settings.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import saschpe.gamesale.mobile.R

@Suppress("unused")
class NotificationPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) =
        addPreferencesFromResource(R.xml.pref_notification)
}
