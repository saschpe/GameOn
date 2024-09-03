package testing

import android.view.View
import androidx.appcompat.widget.ActionBarContainer
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anyOf

/**
 * Custom view matchers are not present in the Espresso library.
 */

fun inActionBar(): Matcher<View> = isDescendantOfA(
    anyOf(
        isAssignableFrom(ActionBarContainer::class.java),
        isAssignableFrom(Toolbar::class.java),
    )
)

fun inTabLayout(): Matcher<View> = isDescendantOfA(isAssignableFrom(TabLayout::class.java))
