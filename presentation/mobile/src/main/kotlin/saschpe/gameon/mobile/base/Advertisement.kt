package saschpe.gameon.mobile.base

import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.formats.UnifiedNativeAd
import saschpe.gameon.mobile.BuildConfig

private val TEST_DEVICE_IDS = listOf(
    AdRequest.DEVICE_ID_EMULATOR, // All emulators
    "6017F914680B8E8A9B332558F8E53245", // Sascha's Galaxy S2
    "CB380BC5777E545490CF0D4A435348D7", // Sascha's OnePlus One
    "207150B4835EC8F5A5FB253CE003B4BE" // Sascha's Pixel C
)

val defaultRequestConfigurationBuilder: RequestConfiguration.Builder =
    RequestConfiguration.Builder()
        .setTestDeviceIds(TEST_DEVICE_IDS)

private const val TEST_NATIVE_ADVANCED_ADD_UNIT = "ca-app-pub-9045162269320751/1893886932"

sealed class NativeAdUnit(
    private val _adUnit: String
) {
    object Offers : NativeAdUnit("ca-app-pub-9045162269320751/1893886932")
    object Search : NativeAdUnit("ca-app-pub-9045162269320751/5590743342")
    object Test : NativeAdUnit(TEST_NATIVE_ADVANCED_ADD_UNIT)

    val adUnit: String
        get() = when {
            BuildConfig.DEBUG -> TEST_NATIVE_ADVANCED_ADD_UNIT
            else -> _adUnit
        }
}

fun Fragment.loadAdvertisement(nativeAdUnit: NativeAdUnit, onLoad: (UnifiedNativeAd) -> Unit) {
    AdLoader.Builder(requireContext(), nativeAdUnit.adUnit)
        .forUnifiedNativeAd { onLoad.invoke(it) }
        .build()
        .loadAd(AdRequest.Builder().build())
}