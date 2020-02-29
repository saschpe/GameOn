package saschpe.gameon.common

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


private val EMAIL_REGEX =
    Regex("""[a-zA-Z0-9\+\.\_\%\-\+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+""")

fun String.isValidEmail() = EMAIL_REGEX.matches(this)
fun String.isValidPassword() = length > 7