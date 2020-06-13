package saschpe.gameon.wear.favorites

import kotlinx.android.synthetic.main.activity_favorites.*
import saschpe.gameon.wear.R
import saschpe.gameon.wear.base.BaseActivity
import saschpe.gameon.wear.base.ClockAmbientCallback
import saschpe.gameon.wear.base.TOP_NAVIGATION_FAVORITES_POSITION

class FavoritesActivity : BaseActivity(R.layout.activity_favorites, TOP_NAVIGATION_FAVORITES_POSITION) {
    override fun getAmbientCallback() = ClockAmbientCallback(drawerLayout, topNavigationDrawer, theme, clock)
}
