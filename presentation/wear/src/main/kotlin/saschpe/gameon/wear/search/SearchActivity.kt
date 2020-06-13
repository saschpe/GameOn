package saschpe.gameon.wear.search

import android.os.Bundle
import androidx.wear.widget.WearableLinearLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import saschpe.gameon.wear.R
import saschpe.gameon.wear.base.BaseActivity
import saschpe.gameon.wear.base.ClockAmbientCallback
import saschpe.gameon.wear.base.TOP_NAVIGATION_SEARCH_POSITION

class SearchActivity : BaseActivity(R.layout.activity_search, TOP_NAVIGATION_SEARCH_POSITION) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView.layoutManager = WearableLinearLayoutManager(this)
    }

    override fun getAmbientCallback() = ClockAmbientCallback(drawerLayout, topNavigationDrawer, theme, clock)
}
