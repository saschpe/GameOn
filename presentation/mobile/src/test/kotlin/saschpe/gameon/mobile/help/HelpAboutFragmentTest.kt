package saschpe.gameon.mobile.help

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.setupContentProvider
import saschpe.gameon.common.base.content.AppContentProvider
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.CustomTabs
import saschpe.gameon.mobile.help.about.HelpAboutFragment
import saschpe.gameon.common.R as CommonR

@RunWith(AndroidJUnit4::class)
class HelpAboutFragmentTest {
    @Before
    fun beforeFirebaseAnalytics() {
        setupContentProvider(AppContentProvider::class.java)
        mockkObject(firebaseAnalytics)
        every { firebaseAnalytics.logEvent(any(), any()) } just Runs
    }

    @Before
    fun beforeCustomTabs() {
        mockkObject(CustomTabs)
        coEvery { CustomTabs.openUrl(any(), any()) } returns Unit
    }

    @Test
    fun clickOnPrivacyPolicy() {
        launchFragmentInContainer<HelpAboutFragment>(themeResId = R.style.App_DayNight)

        onView(withText(CommonR.string.privacy_policy)).perform(click())

        coVerify { CustomTabs.openPrivacyPolicy(any()) }
    }

    @Test
    fun clickOnTermsOfService() {
        launchFragmentInContainer<HelpAboutFragment>(themeResId = R.style.App_DayNight)

        onView(withText(CommonR.string.terms_of_service)).perform(click())

        coVerify { CustomTabs.openTermsOfService(any()) }
    }

    @Test
    fun clickOnOpenSourceLicenses() {
        launchFragmentInContainer<HelpAboutFragment>(themeResId = R.style.App_DayNight)
        Intents.init()

        onView(withText(CommonR.string.open_source_licenses)).perform(click())

        intended(hasComponent(OssLicensesMenuActivity::class.java.name))
        Intents.release()
    }

    @After
    fun afterTests() = unmockkAll()
}
