package saschpe.gamesale.mobile.help

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gamesale.mobile.R

@RunWith(AndroidJUnit4::class)
class ContactFragmentTest {
    @Before
    fun beforeTests() = Intents.init()

    @Test
    fun clickOnRateUseInThePlayStore() {
        launchFragmentInContainer<ContactFragment>(themeResId = R.style.App_DayNight)

        onView(withText(R.string.rate_us_in_the_play_store)).perform(click())

        intended(hasAction("android.intent.action.VIEW"))
    }

    @Test
    fun clickOnRecommendToAFriend() {
        launchFragmentInContainer<ContactFragment>(themeResId = R.style.App_DayNight)

        onView(withText(R.string.recommend_to_a_friend)).perform(click())

        intended(hasAction("android.intent.action.CHOOSER"))
    }

    @Test
    fun clickOnProvideFeedback() {
        launchFragmentInContainer<ContactFragment>(themeResId = R.style.App_DayNight)

        onView(withText(R.string.provide_feedback)).perform(click())

        intended(hasAction("android.intent.action.CHOOSER"))
    }

    @After
    fun afterTests() = Intents.release()
}
