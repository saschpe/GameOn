package saschpe.gameon.wear.search

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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gameon.wear.R
import saschpe.gameon.wear.favorites.FavoritesActivity
import saschpe.gameon.wear.profile.ProfileActivity

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<SearchActivity>()

    @Test
    fun topNavigationFavoriteButtonIntendedFavoriteActivity() {
        // Arrange
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open(Gravity.TOP))

        // Assert
        onView(withId(R.string.favorites)).check(matches(isDisplayed())).perform(ViewActions.click())

        // Assert
        Intents.intended(IntentMatchers.hasComponent(FavoritesActivity::class.java.name))
    }

    @Test
    fun topNavigationProfileButtonIntendedProfileActivity() {
        // Arrange
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open(Gravity.TOP))

        // Assert
        onView(withId(R.string.profile)).check(matches(isDisplayed())).perform(ViewActions.click())

        // Assert
        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
    }
}
