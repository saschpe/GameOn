package saschpe.gameon.wear.profile

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_profile.*
import saschpe.android.versioninfo.VersionInfoUtils.getFormattedCopyright
import saschpe.android.versioninfo.VersionInfoUtils.getFormattedVersion
import saschpe.gameon.wear.BuildConfig
import saschpe.gameon.wear.R
import saschpe.gameon.wear.base.BaseActivity
import saschpe.gameon.wear.base.TOP_NAVIGATION_PROFILE_POSITION
import saschpe.gameon.wear.base.TopNavigationDrawerAdapter
import saschpe.gameon.wear.base.TopNavigationItemSelectedListener

class ProfileActivity : BaseActivity(R.layout.activity_profile, TOP_NAVIGATION_PROFILE_POSITION) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        copyright.text = resources.getString(
            R.string.copyright_text_template,
            getFormattedVersion(this, packageName, BuildConfig.VERSION_NAME),
            getFormattedCopyright(this, packageName, getString(R.string.copyright_owner))
        )
    }
}
