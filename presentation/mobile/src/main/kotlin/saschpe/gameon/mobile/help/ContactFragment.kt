package saschpe.gameon.mobile.help

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import saschpe.android.socialfragment.app.SocialFragment
import saschpe.gameon.mobile.BuildConfig
import saschpe.gameon.mobile.R

class ContactFragment : Fragment(R.layout.fragment_help_contact) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val socialFragment = SocialFragment.Builder()
            .setApplicationId(BuildConfig.APPLICATION_ID)
            .setApplicationName(context?.getString(R.string.app_name))
            .setContactEmailAddress("sascha+gp@peilicke.de")
            .setHeaderTextAppearance(R.style.TextAppearance_App_Headline5)
            .setIconTint(R.color.color_secondary)
            .build()

        childFragmentManager.beginTransaction()
            .replace(R.id.container, socialFragment)
            .commit()
    }
}
