package saschpe.gameon.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.setupContentProvider

@RunWith(AndroidJUnit4::class)
class DataContentProviderTest {
    @Before
    fun beforeContentProvider() {
        setupContentProvider(DataContentProvider::class.java)
    }

    @Test
    fun expectedTypes() {
        InstrumentationRegistry.getInstrumentation().context?.let {
            assertEquals(it.applicationContext, DataContentProvider.applicationContext)
            assertEquals(it.cacheDir, DataContentProvider.cacheDir)
            assertEquals(it.filesDir, DataContentProvider.filesDir)
        }
        assertTrue(DataContentProvider.cacheDir.isDirectory)
        assertTrue(DataContentProvider.filesDir.isDirectory)
    }
}
