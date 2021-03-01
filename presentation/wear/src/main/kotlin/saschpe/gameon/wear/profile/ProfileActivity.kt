package saschpe.gameon.wear.profile

import android.os.Bundle
import saschpe.android.versioninfo.VersionInfoUtils.getFormattedCopyright
import saschpe.android.versioninfo.VersionInfoUtils.getFormattedVersion
import saschpe.gameon.wear.BuildConfig
import saschpe.gameon.wear.R
import saschpe.gameon.wear.base.BaseActivity
import saschpe.gameon.wear.base.BaseAmbientCallback
import saschpe.gameon.wear.base.TOP_NAVIGATION_PROFILE_POSITION
import saschpe.gameon.wear.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTopNavigation(binding.topNavigation, TOP_NAVIGATION_PROFILE_POSITION)

        binding.copyright.text = resources.getString(
            R.string.copyright_text_template,
            getFormattedVersion(this, packageName, BuildConfig.VERSION_NAME),
            getFormattedCopyright(this, packageName, getString(R.string.copyright_owner))
        )
    }

    override fun getAmbientCallback() = BaseAmbientCallback(binding.drawerLayout, binding.topNavigation, theme)
}
