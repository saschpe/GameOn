package saschpe.gamesale.mobile.base

import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.multidex.MultiDexApplication
import java.text.DateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import saschpe.gamesale.common.content.defaultPreferences
import saschpe.gamesale.mobile.BuildConfig
import saschpe.gamesale.mobile.R

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
    }

    companion object {
        /**
         * Any application-specific intent should use this scheme.
         */
        const val INTENT_SCHEME = "gamesale"

        val DATE_FORMAT: DateFormat = DateFormat.getDateInstance()
    }
}
