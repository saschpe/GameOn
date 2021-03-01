package saschpe.gameon.wear.search

import android.os.Bundle
import androidx.wear.widget.WearableLinearLayoutManager
import saschpe.gameon.wear.base.BaseActivity
import saschpe.gameon.wear.base.ClockAmbientCallback
import saschpe.gameon.wear.base.TOP_NAVIGATION_SEARCH_POSITION
import saschpe.gameon.wear.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTopNavigation(binding.topNavigation, TOP_NAVIGATION_SEARCH_POSITION)

        binding.recyclerView.layoutManager = WearableLinearLayoutManager(this)
    }

    override fun getAmbientCallback() =
        ClockAmbientCallback(binding.drawerLayout, binding.topNavigation, theme, binding.clock)
}
