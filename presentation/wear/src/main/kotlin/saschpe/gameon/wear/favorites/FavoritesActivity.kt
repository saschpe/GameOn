package saschpe.gameon.wear.favorites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.ambient.AmbientModeSupport
import saschpe.gameon.wear.base.ClockAmbientCallback
import saschpe.gameon.wear.base.TOP_NAVIGATION_SEARCH_POSITION
import saschpe.gameon.wear.base.TopNavigationDrawerAdapter
import saschpe.gameon.wear.base.TopNavigationItemSelectedListener
import saschpe.gameon.wear.databinding.ActivityFavoritesBinding

class FavoritesActivity :
    AppCompatActivity(),
    AmbientModeSupport.AmbientCallbackProvider {
    private lateinit var ambientController: AmbientModeSupport.AmbientController
    private lateinit var binding: ActivityFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ambientController = AmbientModeSupport.attach(this)

        binding.topNavigationDrawer.setAdapter(TopNavigationDrawerAdapter(this))
        binding.topNavigationDrawer.setCurrentItem(TOP_NAVIGATION_SEARCH_POSITION, false)
        binding.topNavigationDrawer.addOnItemSelectedListener(TopNavigationItemSelectedListener(this))
        binding.topNavigationDrawer.controller.peekDrawer()
    }

    override fun getAmbientCallback() = ClockAmbientCallback(binding.drawerLayout, binding.topNavigationDrawer, theme, binding.clock)
}
