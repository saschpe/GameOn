package testing

import androidx.multidex.MultiDexApplication
import androidx.test.core.app.ApplicationProvider
import coil.Coil
import coil.ImageLoader
import java.lang.reflect.Method
import org.robolectric.TestLifecycleApplication

class StubApplication : MultiDexApplication(), TestLifecycleApplication {
    override fun onCreate() {
        // Coil's default image loader depends on CoilContentProvider, which is Java *internal*
        Coil.setDefaultImageLoader(ImageLoader(ApplicationProvider.getApplicationContext()))
    }

    override fun beforeTest(method: Method) = Unit
    override fun prepareTest(test: Any) = Unit
    override fun afterTest(method: Method) = Unit
}
