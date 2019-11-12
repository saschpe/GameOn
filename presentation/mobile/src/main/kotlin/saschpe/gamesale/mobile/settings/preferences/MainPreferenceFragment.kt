package saschpe.gamesale.mobile.settings.preferences

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import saschpe.gamesale.mobile.BuildConfig
import saschpe.gamesale.mobile.R
import saschpe.gamesale.mobile.settings.SettingsViewModel

class MainPreferenceFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModels({ requireParentFragment() })
    private val preferenceClickListener = { pref: Preference ->
        viewModel.selectedPreference.value = pref
        true
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_main)
        // Remove 'Developer' settings when not in debug build(s)
        if (!BuildConfig.DEBUG) {
            findPreference<Preference>(getString(R.string.pref_developer_key))?.isVisible = false
        }

        findPreference<Preference>(getString(R.string.pref_help_and_feedback_key))?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_helpFragment)
            true
        }

        // Workaround for https://issuetracker.google.com/issues/139995974
        listOf(
            R.string.pref_appearance_key,
            R.string.pref_notification_key,
            R.string.pref_developer_key
        ).forEach {
            findPreference<Preference>(getString(it))?.setOnPreferenceClickListener(
                preferenceClickListener
            )
        }
    }
}
