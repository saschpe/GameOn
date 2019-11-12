package saschpe.gamesale.common.app

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

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
