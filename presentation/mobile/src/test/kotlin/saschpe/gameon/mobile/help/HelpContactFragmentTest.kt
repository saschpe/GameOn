package saschpe.gameon.mobile.help

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.setupContentProvider
import saschpe.gameon.common.content.AppContentProvider
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.help.contact.HelpContactFragment

@RunWith(AndroidJUnit4::class)
class HelpContactFragmentTest {
    @Before
    fun beforeFirebaseAnalytics() {
        setupContentProvider(AppContentProvider::class.java)
        mockkObject(firebaseAnalytics)
        every { firebaseAnalytics.setCurrentScreen(any(), any(), any()) } just Runs
    }

    @Before
    fun beforeIntents() = Intents.init()

    @Test
    fun clickOnRateUseInThePlayStore() {
        launchFragmentInContainer<HelpContactFragment>(themeResId = R.style.App_DayNight)

        onView(withText(R.string.rate_us_in_the_play_store)).perform(click())

        intended(hasAction("android.intent.action.VIEW"))
    }

    @Test
    fun clickOnRecommendToAFriend() {
        launchFragmentInContainer<HelpContactFragment>(themeResId = R.style.App_DayNight)

        onView(withText(R.string.recommend_to_a_friend)).perform(click())

        intended(hasAction("android.intent.action.CHOOSER"))
    }

    @Test
    fun clickOnProvideFeedback() {
        launchFragmentInContainer<HelpContactFragment>(themeResId = R.style.App_DayNight)

        onView(withText(R.string.provide_feedback)).perform(click())

        intended(hasAction("android.intent.action.CHOOSER"))
    }

    @After
    fun afterIntents() = Intents.release()

    @After
    fun afterTests() = unmockkAll()
}
