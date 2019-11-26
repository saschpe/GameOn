package saschpe.gameon.mobile.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.fragment_help.*
import saschpe.android.versioninfo.widget.VersionInfoDialogFragment
import saschpe.gameon.common.app.appNameTitle
import saschpe.gameon.mobile.BuildConfig
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.CustomTabs
import saschpe.gameon.mobile.help.about.AboutFragment
import saschpe.gameon.mobile.help.contact.ContactFragment

class HelpFragment : Fragment(R.layout.fragment_help) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.run {
            // Compare with deep-links defined in navigation_main.xml
            val page = getString("page") ?: ""
            if (page.isNotBlank()) {
                when (page) {
                    "privacy" -> CustomTabs.openPrivacyPolicy(requireContext())
                    "tos" -> CustomTabs.openTermsOfService(requireContext())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        viewPager.adapter = HelpFragmentPagerAdapter(requireContext(), childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).appNameTitle(appName)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_help, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.version_info -> {
                VersionInfoDialogFragment.newInstance(
                    getString(R.string.app_name),
                    BuildConfig.VERSION_NAME,
                    "Sascha Peilicke",
                    R.mipmap.ic_launcher
                ).show(childFragmentManager, "version_info")
                return true
            }
            R.id.privacyPolicy -> CustomTabs.openPrivacyPolicy(requireContext())
            R.id.termsOfService -> CustomTabs.openTermsOfService(requireContext())
            R.id.openSourceLicenses -> startActivity(
                Intent(requireActivity(), OssLicensesMenuActivity::class.java)
            )
        }
        return false
    }

    private class HelpFragmentPagerAdapter(
        val context: Context,
        fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount() = 2

        override fun getItem(position: Int) = when (position) {
            0 -> AboutFragment()
            else -> ContactFragment()
        }

        override fun getPageTitle(position: Int) = when (position) {
            0 -> context.getString(R.string.about)
            else -> context.getString(R.string.contact)
        }
    }
}
