package saschpe.gameon.common.context

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gameon.common.content.dpToPx
import saschpe.gameon.common.content.hasScreenWidth

@RunWith(AndroidJUnit4::class)
class ContextTest {
    private lateinit var context: Context

    @Before
    fun beforeContext() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun hasScreenWidth_300DP() {
        context.resources.configuration.screenWidthDp = 300

        assertFalse(context.hasScreenWidth(420))
        assertFalse(context.hasScreenWidth(320))
    }

    @Test
    fun hasScreenWidth_400DP() {
        context.resources.configuration.screenWidthDp = 400

        assertFalse(context.hasScreenWidth(420))
        assertTrue(context.hasScreenWidth(320))
    }

    @Test
    fun hasScreenWidth_600DP() {
        context.resources.configuration.screenWidthDp = 600

        assertTrue(context.hasScreenWidth(420))
        assertTrue(context.hasScreenWidth(320))
    }

    @Test
    fun dpToPx() = assertEquals(42, context.dpToPx(42))
}
