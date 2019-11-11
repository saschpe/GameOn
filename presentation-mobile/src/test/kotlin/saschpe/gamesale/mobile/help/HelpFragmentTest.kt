package saschpe.gamesale.mobile.help

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gamesale.mobile.R
import testing.inActionBar
import testing.inTabLayout

@Ignore("https://issuetracker.google.com/issues/139695101")
@RunWith(AndroidJUnit4::class)
class HelpFragmentTest {
    @Test
    fun topAppBarDisplaysLogo() {
        launchFragmentInContainer<HelpFragment>(themeResId = R.style.App_DayNight)

        onView(allOf(inActionBar(), withId(R.id.appName))).check(
            matches(allOf(isDisplayed(), withText(R.string.app_name)))
        )
    }

    @Test
    fun tabLayoutContainsAboutAndContacts() {
        launchFragmentInContainer<HelpFragment>(themeResId = R.style.App_DayNight)

        onView(allOf(inTabLayout(), withText(R.string.about))).check(matches(isDisplayed()))
        onView(allOf(inTabLayout(), withText(R.string.contact))).check(matches(isDisplayed()))
    }
}
