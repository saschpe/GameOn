package testing

import androidx.multidex.MultiDexApplication
import androidx.test.core.app.ApplicationProvider
import coil.Coil
import coil.ImageLoader
import org.robolectric.TestLifecycleApplication
import java.lang.reflect.Method

class StubApplication : MultiDexApplication(), TestLifecycleApplication {
    override fun onCreate() = Unit
    override fun beforeTest(method: Method) = Unit
    override fun prepareTest(test: Any) = Unit
    override fun afterTest(method: Method) = Unit
}
