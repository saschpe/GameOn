package saschpe.gameon.mobile.base.customtabs

import android.content.Context
import android.net.Uri
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback
import saschpe.gameon.mobile.R

object CustomTabs {
    private const val HOMEPAGE_URL = "https://www.lunchup.io"
    private const val PRIVACY_POLICY_URL = "https://www.lunchup.io/legal/privacy"
    private const val TERMS_OF_SERVICE_URL = "https://www.lunchup.io/legal/terms"

    private val defaultCustomTabsIntentBuilder = CustomTabsIntent.Builder()
        .addDefaultShareMenuItem()
        .setShowTitle(true)

    fun openHomepage(context: Context) = openUrl(context, HOMEPAGE_URL)
    fun openPrivacyPolicy(context: Context) = openUrl(context, PRIVACY_POLICY_URL)
    fun openTermsOfService(context: Context) = openUrl(context, TERMS_OF_SERVICE_URL)

    fun openUrl(context: Context, url: String) {
        defaultCustomTabsIntentBuilder
            .setToolbarColor(ContextCompat.getColor(context, R.color.color_surface))
            .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)

        AppCompatResources.getDrawable(context, R.drawable.ic_arrow_back_24dp)?.mutate()?.let {
            DrawableCompat.setTint(it, ContextCompat.getColor(context, R.color.color_on_surface))
            R.styleable.SignInButton_colorScheme
            defaultCustomTabsIntentBuilder.setCloseButtonIcon(it.toBitmap())
        }

        val customTabsIntent = defaultCustomTabsIntentBuilder
            .build()

        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent)
        CustomTabsHelper.openCustomTab(context, customTabsIntent, Uri.parse(url), WebViewFallback())
    }
}
