package saschpe.gameon.wear.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.widget.drawer.WearableNavigationDrawerView

/**
 * Base wearable activity.
 *
 * Supports top navigation drawer and ambient mode.
 */
abstract class BaseActivity : AppCompatActivity(), AmbientModeSupport.AmbientCallbackProvider {
    private lateinit var ambientController: AmbientModeSupport.AmbientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ambientController = AmbientModeSupport.attach(this)
    }

    fun initTopNavigation(navigation: WearableNavigationDrawerView, drawerPosition: Int) {
        navigation.setAdapter(TopNavigationDrawerAdapter(this))
        navigation.setCurrentItem(drawerPosition, false)
        navigation.addOnItemSelectedListener(TopNavigationItemSelectedListener(this))
        navigation.controller.peekDrawer()
    }
}