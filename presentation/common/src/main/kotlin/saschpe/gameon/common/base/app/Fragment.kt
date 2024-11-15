package saschpe.gameon.common.base.app

import android.view.WindowManager
import androidx.fragment.app.Fragment

fun Fragment.enableScreenshots() = requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

fun Fragment.disableScreenshots() = requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
