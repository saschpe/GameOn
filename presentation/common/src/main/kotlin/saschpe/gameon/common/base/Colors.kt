package saschpe.gameon.common.base

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import saschpe.gameon.common.R

class Colors(context: Context) {
    @ColorInt
    var amber = ContextCompat.getColor(context, R.color.amber)

    @ColorInt
    var green = ContextCompat.getColor(context, R.color.green)

    @ColorInt
    var onSurface = ContextCompat.getColor(context, R.color.color_on_surface)

    @ColorInt
    var primary = ContextCompat.getColor(context, R.color.color_primary)

    @ColorInt
    var red = ContextCompat.getColor(context, R.color.red)

    @ColorInt
    var secondary = ContextCompat.getColor(context, R.color.color_secondary)

    @ColorInt
    var surface = ContextCompat.getColor(context, R.color.color_surface)
}
