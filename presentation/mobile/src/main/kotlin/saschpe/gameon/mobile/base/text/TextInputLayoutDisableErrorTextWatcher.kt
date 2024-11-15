package saschpe.gameon.mobile.base.text

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

class TextInputLayoutDisableErrorTextWatcher(private val inputLayout: TextInputLayout?) : TextWatcher {
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        inputLayout?.isErrorEnabled = false
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) = Unit

    override fun afterTextChanged(editable: Editable) = Unit
}
