package saschpe.gameon.wear.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.ambient.AmbientModeSupport
import saschpe.android.versioninfo.VersionInfoUtils.getFormattedCopyright
import saschpe.android.versioninfo.VersionInfoUtils.getFormattedVersion
import saschpe.gameon.wear.BuildConfig
import saschpe.gameon.wear.R
import saschpe.gameon.wear.base.BaseAmbientCallback
import saschpe.gameon.wear.base.TOP_NAVIGATION_SEARCH_POSITION
import saschpe.gameon.wear.base.TopNavigationDrawerAdapter
import saschpe.gameon.wear.base.TopNavigationItemSelectedListener
import saschpe.gameon.wear.databinding.ActivityProfileBinding
import saschpe.gameon.common.R as CommonR

class ProfileActivity :
    AppCompatActivity(),
    AmbientModeSupport.AmbientCallbackProvider {
    private lateinit var ambientController: AmbientModeSupport.AmbientController
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ambientController = AmbientModeSupport.attach(this)

        binding.topNavigationDrawer.setAdapter(TopNavigationDrawerAdapter(this))
        binding.topNavigationDrawer.setCurrentItem(TOP_NAVIGATION_SEARCH_POSITION, false)
        binding.topNavigationDrawer.addOnItemSelectedListener(TopNavigationItemSelectedListener(this))
        binding.topNavigationDrawer.controller.peekDrawer()
        binding.copyright.text = resources.getString(
            R.string.copyright_text_template,
            getFormattedVersion(this, packageName, BuildConfig.VERSION_NAME),
            getFormattedCopyright(this, packageName, getString(CommonR.string.copyright_owner))
        )
    }

    override fun getAmbientCallback() = BaseAmbientCallback(binding.drawerLayout, binding.topNavigationDrawer, theme)
}
