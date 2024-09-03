package saschpe.gameon.wear.base

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.widget.drawer.WearableDrawerLayout
import androidx.wear.widget.drawer.WearableNavigationDrawerView
import java.text.SimpleDateFormat
import java.util.*

val AMBIENT_DATE_FORMAT = SimpleDateFormat("HH:mm", Locale.US)

open class BaseAmbientCallback(
    private val drawerLayout: WearableDrawerLayout,
    private val topNavigationDrawer: WearableNavigationDrawerView,
    private val theme: Resources.Theme,
) : AmbientModeSupport.AmbientCallback() {
    override fun onEnterAmbient(ambientDetails: Bundle?) {
        topNavigationDrawer.controller.closeDrawer()
        // drawerLayout.setBackgroundColor(drawerLayout.resources.getColor(R.color.wear_color_background_ambient, theme))
    }

    override fun onUpdateAmbient() = Unit

    override fun onExitAmbient() {
        // drawerLayout.setBackgroundColor(drawerLayout.resources.getColor(R.color.wear_color_background, theme))
        topNavigationDrawer.controller.closeDrawer()
    }
}

class ClockAmbientCallback(
    drawerLayout: WearableDrawerLayout,
    topNavigationDrawer: WearableNavigationDrawerView,
    theme: Resources.Theme,
    private val clock: TextView,
) : BaseAmbientCallback(drawerLayout, topNavigationDrawer, theme) {
    override fun onEnterAmbient(ambientDetails: Bundle?) {
        super.onEnterAmbient(ambientDetails)
        clock.text = AMBIENT_DATE_FORMAT.format(Date())
        clock.visibility = View.VISIBLE
    }

    override fun onUpdateAmbient() {
        super.onUpdateAmbient()
        clock.text = AMBIENT_DATE_FORMAT.format(Date())
    }

    override fun onExitAmbient() {
        super.onExitAmbient()
        clock.visibility = View.GONE
    }
}
