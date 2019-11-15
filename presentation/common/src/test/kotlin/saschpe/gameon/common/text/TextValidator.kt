package saschpe.gameon.common.text

import android.text.Editable
import android.text.TextWatcher

abstract class TextValidator : TextWatcher {
    abstract fun validate(text: String)

    override fun afterTextChanged(s: Editable) = validate(s.toString())

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
}
