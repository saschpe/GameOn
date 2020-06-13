package saschpe.gameon.wear.favorites

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gameon.wear.R
import saschpe.gameon.wear.profile.ProfileActivity
import saschpe.gameon.wear.search.SearchActivity

@RunWith(AndroidJUnit4::class)
class FavoriteActivityTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<FavoritesActivity>()

    @Test
    fun clockIsInvisibleByDefault() {
        // Arrange, act, assert
        onView(withId(R.id.clock)).check(matches(not(isDisplayed())))
    }

    @Test
    fun topNavigationSearchButtonIntendedSearchActivity() {
        // Arrange
        onView(withId(R.id.drawerLayout)).perform(open(Gravity.TOP))

        // Assert
        onView(withId(R.string.search)).check(matches(isDisplayed())).perform(click())

        // Assert
        intended(hasComponent(SearchActivity::class.java.name))
    }

    @Test
    fun topNavigationProfileButtonIntendedProfileActivity() {
        // Arrange
        onView(withId(R.id.drawerLayout)).perform(open(Gravity.TOP))

        // Assert
        onView(withId(R.string.profile)).check(matches(isDisplayed())).perform(click())

        // Assert
        intended(hasComponent(ProfileActivity::class.java.name))
    }
}
