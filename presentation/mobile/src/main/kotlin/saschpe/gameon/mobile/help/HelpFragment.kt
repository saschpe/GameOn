package saschpe.gameon.mobile.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.fragment_help.*
import kotlinx.coroutines.launch
import saschpe.android.versioninfo.widget.VersionInfoDialogFragment
import saschpe.gameon.common.app.appNameTitle
import saschpe.gameon.mobile.BuildConfig
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.openPrivacyPolicy
import saschpe.gameon.mobile.base.customtabs.openTermsOfService
import saschpe.gameon.mobile.help.about.HelpAboutFragment
import saschpe.gameon.mobile.help.contact.HelpContactFragment

open class HelpFragment : Fragment(R.layout.fragment_help) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            // Compare with deep-links defined in navigation_main.xml
            val page = getString("page") ?: ""
            if (page.isNotBlank()) {
                when (page) {
                    "privacy" -> lifecycleScope.launch { openPrivacyPolicy() }
                    "tos" -> lifecycleScope.launch { openTermsOfService() }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(toolbar, findNavController())
        toolbar.inflateMenu(R.menu.menu_help)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.version_info -> VersionInfoDialogFragment.newInstance(
                    getString(R.string.app_name),
                    BuildConfig.VERSION_NAME,
                    "Sascha Peilicke",
                    R.mipmap.ic_launcher
                ).show(childFragmentManager, "version_info")
                R.id.privacyPolicy -> lifecycleScope.launch { openPrivacyPolicy() }
                R.id.termsOfService -> lifecycleScope.launch { openTermsOfService() }
                R.id.openSourceLicenses -> startActivity(
                    Intent(requireActivity(), OssLicensesMenuActivity::class.java)
                )
            }
            true
        }

        viewPager.adapter = HelpFragmentPagerAdapter(requireContext(), childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Help", "HelpFragment")
        requireActivity().appNameTitle(appName)
    }

    private class HelpFragmentPagerAdapter(
        val context: Context, fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount() = 2

        override fun getItem(position: Int) = when (position) {
            0 -> HelpAboutFragment()
            else -> HelpContactFragment()
        }

        override fun getPageTitle(position: Int) = when (position) {
            0 -> context.getString(R.string.about)
            else -> context.getString(R.string.contact)
        }
    }
}
