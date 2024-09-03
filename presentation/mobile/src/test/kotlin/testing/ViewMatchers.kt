@file:Suppress("MemberVisibilityCanBePrivate")

package testing

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.ActionBarContainer
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anyOf
import org.hamcrest.TypeSafeMatcher
import saschpe.gameon.common.base.tinted
import saschpe.gameon.common.base.toColorInt

/**
 * Custom view matchers are not present in the Espresso library.
 */

fun withDrawable(
    @DrawableRes id: Int,
    @ColorRes tint: Int? = null,
    tintMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN
) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("ImageView with drawable same as drawable with id $id")
        tint?.let { description.appendText(", tint color id: $tint, mode: $tintMode") }
    }

    override fun matchesSafely(view: View): Boolean {
        val context = view.context
        val tintColor = tint?.toColorInt(context)
        val expectedBitmap = context?.getDrawable(id)?.tinted(tintColor, tintMode)?.toBitmap()

        return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
    }
}

fun withHint(expected: String) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("TextView or TextInputLayout with hint '$expected'")
    }

    override fun matchesSafely(item: View) =
        item is TextInputLayout && expected == item.hint || item is TextView && expected == item.hint
}

fun withHelperText(expected: String) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("TextInputLayout with helper text '$expected'")
    }

    override fun matchesSafely(item: View) =
        item is TextInputLayout && expected == item.helperText
}

fun inActionBar(): Matcher<View> = isDescendantOfA(
    anyOf(
        isAssignableFrom(ActionBarContainer::class.java),
        isAssignableFrom(Toolbar::class.java)
    )
)

fun inTabLayout(): Matcher<View> = isDescendantOfA(isAssignableFrom(TabLayout::class.java))
