package saschpe.gameon.wear.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.ambient.AmbientModeSupport
import kotlinx.android.synthetic.main.activity_profile.*

/**
 * Base wearable activity.
 *
 * Supports top navigation drawer and ambient mode.
 */
abstract class BaseActivity(
    @LayoutRes private val layoutId: Int,
    private val topNavigationDrawerPosition: Int
) : AppCompatActivity(), AmbientModeSupport.AmbientCallbackProvider {
    private lateinit var ambientController: AmbientModeSupport.AmbientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        ambientController = AmbientModeSupport.attach(this)

        topNavigationDrawer.setAdapter(TopNavigationDrawerAdapter(this))
        topNavigationDrawer.setCurrentItem(topNavigationDrawerPosition, false)
        topNavigationDrawer.addOnItemSelectedListener(TopNavigationItemSelectedListener(this))
        topNavigationDrawer.controller.peekDrawer()
    }

    override fun getAmbientCallback() = BaseAmbientCallback(drawerLayout, topNavigationDrawer, theme)
}