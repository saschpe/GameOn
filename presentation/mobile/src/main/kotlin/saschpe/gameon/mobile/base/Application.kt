package saschpe.gameon.mobile.base

import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import saschpe.gameon.common.content.defaultPreferences
import saschpe.gameon.mobile.BuildConfig
import saschpe.gameon.mobile.R
import saschpe.log4k.ConsoleLogger
import saschpe.log4k.Log
import java.text.DateFormat

class Application : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
        }

        GlobalScope.launch {
            // Set default night mode on start-up.
            setDefaultNightMode(withContext(Dispatchers.IO) {
                defaultPreferences.getString(
                    getString(R.string.pref_theme_key),
                    resources.getString(R.string.pref_theme_default)
                )?.toInt() ?: -1
            })
        }

        Log.loggers.add(ConsoleLogger().apply {
            if (!BuildConfig.DEBUG) {
                minimumLogLevel = Log.Level.Info
            }
        })
    }
}
