package saschpe.gameon.mobile.help.about

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.launch
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.openPrivacyPolicy
import saschpe.gameon.mobile.base.customtabs.openTermsOfService
import saschpe.gameon.mobile.databinding.FragmentHelpAboutBinding
import java.util.*
import saschpe.gameon.common.R as CommonR

class HelpAboutFragment : Fragment(R.layout.fragment_help_about) {
    private var _binding: FragmentHelpAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHelpAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.privacyPolicy.setOnClickListener { lifecycleScope.launch { openPrivacyPolicy() } }
        binding.termsOfService.setOnClickListener { lifecycleScope.launch { openTermsOfService() } }
        binding.openSourceLicenses.setOnClickListener {
            startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
        }

        binding.disclaimer.movementMethod = LinkMovementMethod()
        binding.disclaimer.text = HtmlCompat.fromHtml(
            getString(CommonR.string.disclaimer_text),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.copyrightText.text =
            getString(CommonR.string.copyright_notice_template, Calendar.getInstance().get(Calendar.YEAR))
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Help About")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "HelpAboutFragment")
        }
    }
}
