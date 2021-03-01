package saschpe.gameon.mobile.help.contact

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import saschpe.android.socialfragment.app.SocialFragment
import saschpe.gameon.mobile.BuildConfig
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R

class HelpContactFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val socialFragment = SocialFragment.Builder()
            .setApplicationId(BuildConfig.APPLICATION_ID)
            .setApplicationName(context?.getString(R.string.app_name))
            .setContactEmailAddress("sascha+gameon@peilicke.de")
            .setHeaderTextAppearance(R.style.TextAppearance_App_Headline5)
            .setIconTint(R.color.color_secondary)
            .build()

        childFragmentManager.beginTransaction()
            .replace(R.id.container, socialFragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Help Contact")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "HelpContactFragment")
        }
    }
}
