package saschpe.gameon.mobile.base.customtabs

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback
import saschpe.gameon.common.Module.colors
import saschpe.gameon.common.content.defaultPreferences
import saschpe.gameon.mobile.R
import saschpe.log4k.Log

object CustomTabs {
    private const val HOMEPAGE_URL = "https://sites.google.com/view/gameon-2"
    private const val PRIVACY_POLICY_URL = "https://sites.google.com/view/gameon-2/legal/privacy"
    private const val TERMS_OF_SERVICE_URL = "https://sites.google.com/view/gameon-2/legal/terms"

    suspend fun openHomepage(context: Context) = openUrl(context, HOMEPAGE_URL)
    suspend fun openPrivacyPolicy(context: Context) = openUrl(context, PRIVACY_POLICY_URL)
    suspend fun openTermsOfService(context: Context) = openUrl(context, TERMS_OF_SERVICE_URL)

    suspend fun openUrl(context: Context, url: String) {
        Log.info("Opening URL '$url'...")

        val nightMode = withContext(Dispatchers.IO) {
            context.defaultPreferences.getString(
                context.getString(R.string.pref_theme_key),
                context.resources.getString(R.string.pref_theme_default)
            )?.toInt() ?: -1
        }

        val darkMode = when (nightMode) {
            MODE_NIGHT_YES -> CustomTabsIntent.COLOR_SCHEME_DARK
            MODE_NIGHT_NO -> CustomTabsIntent.COLOR_SCHEME_LIGHT
            else -> CustomTabsIntent.COLOR_SCHEME_SYSTEM
        }

        val builder = CustomTabsIntent.Builder()
            .addDefaultShareMenuItem()
            .setShowTitle(true)
            .setColorScheme(darkMode)
            .setColorSchemeParams(
                CustomTabsIntent.COLOR_SCHEME_DARK,
                CustomTabColorSchemeParams.Builder().setToolbarColor(colors.surface).build()
            )
            .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)

        context.resources.getDrawable(R.drawable.ic_arrow_back_24dp, null)?.mutate()?.let {
            DrawableCompat.setTint(it, colors.onSurface)
            R.styleable.SignInButton_colorScheme
            builder.setCloseButtonIcon(it.toBitmap())
        }

        val customTabsIntent = builder.build()

        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent)
        CustomTabsHelper.openCustomTab(context, customTabsIntent, Uri.parse(url), WebViewFallback())
    }
}

suspend fun Fragment.openUrl(url: String) = CustomTabs.openUrl(requireContext(), url)
suspend fun Fragment.openPrivacyPolicy() = CustomTabs.openPrivacyPolicy(requireContext())
suspend fun Fragment.openTermsOfService() = CustomTabs.openTermsOfService(requireContext())
