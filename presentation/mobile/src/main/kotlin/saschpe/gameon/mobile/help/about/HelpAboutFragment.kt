package saschpe.gameon.mobile.help.about

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.fragment_help_about.*
import kotlinx.coroutines.launch
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.openPrivacyPolicy
import saschpe.gameon.mobile.base.customtabs.openTermsOfService
import java.time.LocalDate

class HelpAboutFragment : Fragment(R.layout.fragment_help_about) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        privacyPolicy.setOnClickListener { lifecycleScope.launch { openPrivacyPolicy() } }
        termsOfService.setOnClickListener { lifecycleScope.launch { openTermsOfService() } }
        openSourceLicenses.setOnClickListener {
            startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
        }

        disclaimer.movementMethod = LinkMovementMethod()
        disclaimer.text = HtmlCompat.fromHtml(
            getString(R.string.disclaimer_text), HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        copyrightText.text = getString(R.string.copyright_notice_template, LocalDate.now().year)
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Help About", "HelpAboutFragment")
    }
}
