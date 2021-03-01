package saschpe.gameon.mobile.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.launch
import saschpe.android.versioninfo.widget.VersionInfoDialogFragment
import saschpe.gameon.common.base.app.appNameTitle
import saschpe.gameon.mobile.BuildConfig
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.openPrivacyPolicy
import saschpe.gameon.mobile.base.customtabs.openTermsOfService
import saschpe.gameon.mobile.databinding.FragmentHelpBinding
import saschpe.gameon.mobile.help.about.HelpAboutFragment
import saschpe.gameon.mobile.help.contact.HelpContactFragment

open class HelpFragment : Fragment() {
    private lateinit var binding: FragmentHelpBinding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.inflateMenu(R.menu.menu_help)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.version_info -> VersionInfoDialogFragment.newInstance(
                    getString(R.string.app_name),
                    BuildConfig.VERSION_NAME,
                    "Sascha Peilicke",
                    R.mipmap.ic_launcher
                ).show(childFragmentManager, "version_info")
                R.id.privacy_policy -> lifecycleScope.launch { openPrivacyPolicy() }
                R.id.terms_of_service -> lifecycleScope.launch { openTermsOfService() }
                R.id.open_source_licenses -> startActivity(
                    Intent(requireActivity(), OssLicensesMenuActivity::class.java)
                )
            }
            true
        }

        binding.viewPager.adapter = HelpFragmentPagerAdapter(requireContext(), childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Help")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "HelpFragment")
        }
        requireActivity().appNameTitle(binding.appName)
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
