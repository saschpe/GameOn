package saschpe.gameon.wear.favorites

import android.os.Bundle
import saschpe.gameon.wear.base.BaseActivity
import saschpe.gameon.wear.base.ClockAmbientCallback
import saschpe.gameon.wear.base.TOP_NAVIGATION_FAVORITES_POSITION
import saschpe.gameon.wear.databinding.ActivityFavoritesBinding

class FavoritesActivity : BaseActivity() {
    private lateinit var binding: ActivityFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTopNavigation(binding.topNavigation, TOP_NAVIGATION_FAVORITES_POSITION)
    }

    override fun getAmbientCallback() =
        ClockAmbientCallback(binding.drawerLayout, binding.topNavigation, theme, binding.clock)
}
