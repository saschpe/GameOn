package saschpe.gamesale.mobile.settings.preferences

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import saschpe.gamesale.mobile.R

class AppearancePreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_appearance, rootKey)

        findPreference<Preference>(getString(R.string.pref_theme_key))?.apply {
            summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
            setOnPreferenceChangeListener { _, value ->
                if (value is String) {
                    AppCompatDelegate.setDefaultNightMode(value.toInt())
                }
                true
            }
        }
    }
}
