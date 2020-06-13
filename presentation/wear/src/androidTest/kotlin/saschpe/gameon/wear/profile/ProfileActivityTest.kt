package saschpe.gameon.wear.profile

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gameon.wear.R
import saschpe.gameon.wear.favorites.FavoriteActivityTest
import saschpe.gameon.wear.favorites.FavoritesActivity
import saschpe.gameon.wear.profile.ProfileActivity
import saschpe.gameon.wear.search.SearchActivity

@RunWith(AndroidJUnit4::class)
class ProfileActivityTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<ProfileActivity>()

    @Test
    fun copyrightIsDisplayedAndContainsText() {
        val scenario = activityScenarioRule.scenario

        scenario.onActivity {

        }

        onView(withId(R.id.copyright)).check(matches(allOf(isDisplayed())))
    }

    @Test
    fun topNavigationFavoriteButtonIntendedFavoriteActivity() {
        // Arrange
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open(Gravity.TOP))

        // Assert
        onView(withId(R.string.favorites)).check(matches(isDisplayed())).perform(ViewActions.click())

        // Assert
        Intents.intended(IntentMatchers.hasComponent(FavoritesActivity::class.java.name))
    }

    @Test
    fun topNavigationSearchButtonIntendedSearchActivity() {
        // Arrange
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open(Gravity.TOP))

        // Assert
        onView(withId(R.string.search)).check(matches(isDisplayed())).perform(ViewActions.click())

        // Assert
        Intents.intended(IntentMatchers.hasComponent(SearchActivity::class.java.name))
    }
}
