package testing

import androidx.multidex.MultiDexApplication
import org.robolectric.TestLifecycleApplication
import java.lang.reflect.Method

class StubApplication : MultiDexApplication(), TestLifecycleApplication {
    override fun onCreate() = Unit
    override fun beforeTest(method: Method) = Unit
    override fun prepareTest(test: Any) = Unit
    override fun afterTest(method: Method) = Unit
}
