package saschpe.gameon.common.app

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import saschpe.gameon.common.R

fun Activity.hideSoftInputFromWindow() {
    val view = currentFocus
    if (view != null) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun AppCompatActivity.appNameTitle(textView: TextView) {
    val firstColor = ContextCompat.getColor(this, R.color.color_primary)
    val secondColor = ContextCompat.getColor(this, R.color.color_secondary)

    title = ""
    textView.text = HtmlCompat.fromHtml(
        getString(R.string.app_name_template, firstColor, secondColor),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}