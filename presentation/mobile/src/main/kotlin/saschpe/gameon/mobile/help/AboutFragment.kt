package saschpe.gameon.mobile.help

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.fragment_help_about.*
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.CustomTabs

class AboutFragment : Fragment(R.layout.fragment_help_about) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        privacyPolicy.setOnClickListener { CustomTabs.openPrivacyPolicy(requireContext()) }
        termsOfService.setOnClickListener { CustomTabs.openTermsOfService(requireContext()) }
        openSourceLicenses.setOnClickListener {
            startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
        }

        disclaimer.text = HtmlCompat.fromHtml(
            getString(R.string.lorem_ipsum_long), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        disclaimer.movementMethod = LinkMovementMethod()
    }
}
