package saschpe.gameon.wear.base

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_profile.*
import saschpe.gameon.wear.R

/**
 * Base wearable activity.
 *
 * Supports top navigation drawer and ambient mode.
 */
abstract class BaseActivity(
    @LayoutRes private val layoutId: Int,
    private val topNavigationDrawerPosition: Int
) : WearableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setAmbientEnabled()

        topNavigationDrawer.setAdapter(TopNavigationDrawerAdapter(this))
        topNavigationDrawer.setCurrentItem(topNavigationDrawerPosition, false)
        topNavigationDrawer.addOnItemSelectedListener(TopNavigationItemSelectedListener(this))
        topNavigationDrawer.controller.peekDrawer()
    }

    override fun onEnterAmbient(ambientDetails: Bundle?) {
        super.onEnterAmbient(ambientDetails)
        topNavigationDrawer.controller.closeDrawer()
        drawerLayout.setBackgroundColor(resources.getColor(R.color.wear_color_background_ambient, theme))
    }

    override fun onExitAmbient() {
        super.onExitAmbient()
        drawerLayout.setBackgroundColor(resources.getColor(R.color.wear_color_background, theme))
        topNavigationDrawer.controller.closeDrawer()
    }
}