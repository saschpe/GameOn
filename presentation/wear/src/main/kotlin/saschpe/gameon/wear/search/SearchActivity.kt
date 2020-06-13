package saschpe.gameon.wear.search

import android.os.Bundle
import android.view.View
import androidx.wear.widget.WearableLinearLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import saschpe.gameon.wear.R
import saschpe.gameon.wear.base.AMBIENT_DATE_FORMAT
import saschpe.gameon.wear.base.BaseActivity
import saschpe.gameon.wear.base.TOP_NAVIGATION_SEARCH_POSITION
import java.util.*

class SearchActivity : BaseActivity(R.layout.activity_search, TOP_NAVIGATION_SEARCH_POSITION) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView.layoutManager = WearableLinearLayoutManager(this)
    }

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
