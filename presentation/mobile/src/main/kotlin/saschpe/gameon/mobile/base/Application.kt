package saschpe.gameon.mobile.base

import android.util.Log.*
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.*
import saschpe.gameon.common.base.content.defaultPreferences
import saschpe.gameon.mobile.BuildConfig
import saschpe.gameon.mobile.Module.workManager
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.favorites.PriceAlertsWorker
import saschpe.log4k.ConsoleLogger
import saschpe.log4k.Log
import saschpe.log4k.Logger

class Application : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        initLogging()
        initNightMode()
        initWorkManager()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope.cancel("onLowMemory() called by system")
        applicationScope = MainScope()
    }

    private fun initLogging() {
        Log.loggers += when {
            BuildConfig.DEBUG -> ConsoleLogger()
            else -> CrashlyticsLogger()
        }
    }

    private fun initNightMode() = applicationScope.launch {
        setDefaultNightMode(
            withContext(Dispatchers.IO) {
                defaultPreferences.getString(
                    getString(R.string.pref_theme_key),
                    resources.getString(R.string.pref_theme_default)
                )?.toInt() ?: -1
            }
        )
    }

    private fun initWorkManager() {
        PriceAlertsWorker.enqueueOnce(workManager)
        PriceAlertsWorker.enqueuePeriodic(workManager)
    }

    private class CrashlyticsLogger : Logger() {
        override fun print(level: Log.Level, tag: String, message: String?, throwable: Throwable?) {
            val priority = when (level) {
                Log.Level.Verbose -> VERBOSE
                Log.Level.Debug -> DEBUG
                Log.Level.Info -> INFO
                Log.Level.Warning -> WARN
                Log.Level.Error -> ERROR
                Log.Level.Assert -> ASSERT
            }
            if (priority >= ERROR) {
                FirebaseCrashlytics.getInstance().log("$priority $tag $message")
                throwable?.let { FirebaseCrashlytics.getInstance().recordException(it) }
            }
        }
    }

    companion object {
        /**
         * Any application-specific intent should use this scheme.
         */
        const val INTENT_SCHEME = "gameon"

        private var applicationScope = MainScope()
    }
}
