package saschpe.gameon.wear.base

import android.os.StrictMode
import saschpe.gameon.wear.BuildConfig
import saschpe.log4k.ConsoleLogger
import saschpe.log4k.Log

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
        }

        initLogging()
    }

    private fun initLogging() {
        Log.loggers.add(ConsoleLogger().apply {
            if (!BuildConfig.DEBUG) {
                minimumLogLevel = Log.Level.Info
            }
        })
    }
}
