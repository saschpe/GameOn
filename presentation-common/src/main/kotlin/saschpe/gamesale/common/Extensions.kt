package saschpe.gamesale.common

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

@ColorInt
fun Int.toColorInt(context: Context) = ContextCompat.getColor(context, this)

fun Drawable.tinted(@ColorInt tintColor: Int? = null, tintMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) =
    apply {
        setTintList(tintColor?.toColorStateList())
        setTintMode(tintMode)
    }

fun Int.toColorStateList(): ColorStateList = ColorStateList.valueOf(this)
