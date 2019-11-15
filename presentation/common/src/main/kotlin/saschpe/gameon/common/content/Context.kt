package saschpe.gameon.common.content

import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import androidx.preference.PreferenceManager

fun Context.dpToPx(dp: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(), resources.displayMetrics
).toInt()

fun Context.hasScreenWidth(widthInDP: Int) = resources.configuration.screenWidthDp >= widthInDP

val Context.defaultPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)
