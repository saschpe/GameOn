package saschpe.gameon.mobile.base

import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import saschpe.gameon.common.content.defaultPreferences
import saschpe.gameon.mobile.BuildConfig
import saschpe.gameon.mobile.Module.workManager
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.favorites.PriceAlertsWorker
import saschpe.log4k.ConsoleLogger
import saschpe.log4k.Log

class Application : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
        }

        initCrashlytics()
        initLogging()
        initNightMode()
        initWorkManager()
    }

    private fun initCrashlytics() {
        // See https://developer.android.com/studio/build/optimize-your-build#disable_crashlytics
        Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build()
            .also { Fabric.with(this, it) }
    }

    private fun initLogging() {
        Log.loggers.add(ConsoleLogger().apply {
            if (!BuildConfig.DEBUG) {
                minimumLogLevel = Log.Level.Info
            }
        })
    }

    private fun initNightMode() = GlobalScope.launch {
        setDefaultNightMode(withContext(Dispatchers.IO) {
            defaultPreferences.getString(
                getString(R.string.pref_theme_key), resources.getString(R.string.pref_theme_default)
            )?.toInt() ?: -1
        })
    }

    private fun initWorkManager(): Unit {
        PriceAlertsWorker.enqueueOnce(workManager)
        PriceAlertsWorker.enqueuePeriodic(workManager)
    }
}
