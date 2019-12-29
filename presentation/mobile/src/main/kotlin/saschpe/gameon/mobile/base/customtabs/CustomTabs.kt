package saschpe.gameon.mobile.base.customtabs

import android.content.Context
import android.net.Uri
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback
import saschpe.gameon.common.Module.colors
import saschpe.gameon.mobile.R
import saschpe.log4k.Log

object CustomTabs {
    private const val HOMEPAGE_URL = "https://sites.google.com/view/gameon-2"
    private const val PRIVACY_POLICY_URL = "https://sites.google.com/view/gameon-2/legal/privacy"
    private const val TERMS_OF_SERVICE_URL = "https://sites.google.com/view/gameon-2/legal/terms"

    private val defaultCustomTabsIntentBuilder = CustomTabsIntent.Builder()
        .addDefaultShareMenuItem()
        .setShowTitle(true)

    fun openHomepage(context: Context) = openUrl(context, HOMEPAGE_URL)
    fun openPrivacyPolicy(context: Context) = openUrl(context, PRIVACY_POLICY_URL)
    fun openTermsOfService(context: Context) = openUrl(context, TERMS_OF_SERVICE_URL)

    fun openUrl(context: Context, url: String) {
        Log.info("Opening URL '$url'...")
        defaultCustomTabsIntentBuilder
            .setToolbarColor(colors.surface)
            .setNavigationBarColor(colors.surface)
            .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)

        AppCompatResources.getDrawable(context, R.drawable.ic_arrow_back_24dp)?.mutate()?.let {
            DrawableCompat.setTint(it, colors.onSurface)
            R.styleable.SignInButton_colorScheme
            defaultCustomTabsIntentBuilder.setCloseButtonIcon(it.toBitmap())
        }

        val customTabsIntent = defaultCustomTabsIntentBuilder.build()

        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent)
        CustomTabsHelper.openCustomTab(context, customTabsIntent, Uri.parse(url), WebViewFallback())
    }
}
