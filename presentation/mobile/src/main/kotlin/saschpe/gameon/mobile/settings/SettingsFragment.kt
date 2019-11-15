package saschpe.gameon.mobile.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlinx.android.synthetic.main.fragment_settings.*
import saschpe.gameon.common.content.hasScreenWidth
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.settings.preferences.AppearancePreferenceFragment
import saschpe.gameon.mobile.settings.preferences.MainPreferenceFragment

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        val mainPreferenceFragment = MainPreferenceFragment()
        childFragmentManager.commit { replace(R.id.mainContainer, mainPreferenceFragment) }
        if (requireContext().hasScreenWidth(600)) {
            val detailPreferenceFragment = AppearancePreferenceFragment()
            childFragmentManager.commit { replace(R.id.detailContainer, detailPreferenceFragment) }
        }

        viewModel.selectedPreference.observe(this, Observer {
            onPreferenceStartFragment(mainPreferenceFragment, it)
        })
    }

    // Workaround for https://issuetracker.google.com/issues/139995974
    private fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference) {
        val fragment = childFragmentManager.fragmentFactory.instantiate(
            requireContext().classLoader, pref.fragment
        ).apply {
            arguments = pref.extras
            setTargetFragment(caller, 0)
        }

        // Replace the existing Fragment with the new Fragment
        val container = if (requireContext().hasScreenWidth(600)) {
            R.id.detailContainer
        } else {
            R.id.mainContainer
        }
        childFragmentManager.commit {
            replace(container, fragment)
            addToBackStack("preferences")
        }
    }
}
