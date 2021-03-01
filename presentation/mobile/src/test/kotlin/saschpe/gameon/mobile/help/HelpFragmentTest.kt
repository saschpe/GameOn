package saschpe.gameon.mobile.help

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation.setViewNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matchers.allOf
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gameon.mobile.R
import testing.inActionBar
import testing.inTabLayout

@Ignore("Issues remain with mocking NavController")
@RunWith(AndroidJUnit4::class)
class HelpFragmentTest {
    private val navController = mockk<NavController>()
    private val navGraph = mockk<NavGraph>()

    init {
        every { navController.graph } returns navGraph
        every { navGraph.startDestination } returns mockk()
    }

    @Test
    fun topAppBarDisplaysLogo() {
        fragmentScenario()

        onView(allOf(inActionBar(), withId(R.id.app_name))).check(
            matches(allOf(isDisplayed(), withText(R.string.app_name)))
        )
    }

    @Test
    fun tabLayoutContainsAboutAndContacts() {
        fragmentScenario()

        onView(allOf(inTabLayout(), withText(R.string.about))).check(matches(isDisplayed()))
        onView(allOf(inTabLayout(), withText(R.string.contact))).check(matches(isDisplayed()))
    }

    private fun fragmentScenario() =
        launchFragmentInContainer(themeResId = R.style.App_DayNight) {
            HelpFragment().apply {
                viewLifecycleOwnerLiveData.observeForever {
                    setViewNavController(requireView(), navController)
                }
            }
        }
}
