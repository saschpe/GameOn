package saschpe.gameon.wear.base

import android.content.Context
import android.content.Intent
import androidx.wear.widget.drawer.WearableNavigationDrawerView
import saschpe.gameon.wear.R
import saschpe.gameon.wear.favorites.FavoritesActivity
import saschpe.gameon.wear.offers.OffersActivity
import saschpe.gameon.wear.profile.ProfileActivity
import saschpe.gameon.wear.search.SearchActivity

const val TOP_NAVIGATION_FAVORITES_POSITION = 0
const val TOP_NAVIGATION_OFFERS_POSITION = 1
const val TOP_NAVIGATION_SEARCH_POSITION = 2
const val TOP_NAVIGATION_PROFILE_POSITION = 3

class TopNavigationDrawerAdapter(
    private val context: Context
) : WearableNavigationDrawerView.WearableNavigationDrawerAdapter() {
    override fun getItemText(position: Int) = when (position) {
        TOP_NAVIGATION_FAVORITES_POSITION -> context.getString(R.string.favorites)
        TOP_NAVIGATION_OFFERS_POSITION -> context.getString(R.string.offers)
        TOP_NAVIGATION_SEARCH_POSITION -> context.getString(R.string.search)
        TOP_NAVIGATION_PROFILE_POSITION -> context.getString(R.string.profile)
        else -> throw IllegalArgumentException("Unsupported position")
    }

    override fun getItemDrawable(position: Int) = when (position) {
        TOP_NAVIGATION_FAVORITES_POSITION -> context.getDrawable(R.drawable.ic_favorite_border_24dp)
        TOP_NAVIGATION_OFFERS_POSITION -> context.getDrawable(R.drawable.ic_whatshot_24dp)
        TOP_NAVIGATION_SEARCH_POSITION -> context.getDrawable(R.drawable.ic_search_24dp)
        TOP_NAVIGATION_PROFILE_POSITION -> context.getDrawable(R.drawable.ic_account_circle_24dp)
        else -> throw IllegalArgumentException("Unsupported position")
    }

    override fun getCount() = 4
}

class TopNavigationItemSelectedListener(
    private val context: Context
) : WearableNavigationDrawerView.OnItemSelectedListener {
    override fun onItemSelected(position: Int) = when (position) {
        TOP_NAVIGATION_FAVORITES_POSITION -> context.startActivity(Intent(context, FavoritesActivity::class.java))
        TOP_NAVIGATION_OFFERS_POSITION -> context.startActivity(Intent(context, OffersActivity::class.java))
        TOP_NAVIGATION_SEARCH_POSITION -> context.startActivity(Intent(context, SearchActivity::class.java))
        TOP_NAVIGATION_PROFILE_POSITION -> context.startActivity(Intent(context, ProfileActivity::class.java))
        else -> throw IllegalArgumentException("Unsupported position")
    }
}