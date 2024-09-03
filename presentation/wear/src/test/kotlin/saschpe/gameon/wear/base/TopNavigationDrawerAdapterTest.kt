package saschpe.gameon.wear.base

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gameon.wear.R
import saschpe.gameon.common.R as CommonR

@RunWith(AndroidJUnit4::class)
class TopNavigationDrawerAdapterTest {
    private lateinit var adapter: TopNavigationDrawerAdapter
    private lateinit var context: Context

    @Before
    fun beforeAdapter() {
        context = InstrumentationRegistry.getInstrumentation().context
        adapter = TopNavigationDrawerAdapter(context)
    }

    @Test
    fun getCountWithFourItems() = assertEquals(4, adapter.count.toLong())

    @Test
    fun getItemTextWithTwoItems() {
        assertEquals(context.getString(CommonR.string.favorites), adapter.getItemText(TOP_NAVIGATION_FAVORITES_POSITION))
        assertEquals(context.getString(CommonR.string.offers), adapter.getItemText(TOP_NAVIGATION_OFFERS_POSITION))
        assertEquals(context.getString(CommonR.string.search), adapter.getItemText(TOP_NAVIGATION_SEARCH_POSITION))
        assertEquals(context.getString(CommonR.string.profile), adapter.getItemText(TOP_NAVIGATION_PROFILE_POSITION))
    }
}
