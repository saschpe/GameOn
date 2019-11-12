package saschpe.gamesale.mobile.help

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gamesale.mobile.R
import saschpe.gamesale.mobile.base.customtabs.CustomTabs

@RunWith(AndroidJUnit4::class)
class AboutFragmentTest {
    @Before
    fun beforeTests() {
        mockkObject(CustomTabs)
        every { CustomTabs.openUrl(any(), any()) } returns Unit
    }

    @Test
    fun clickOnPrivacyPolicy() {
        launchFragmentInContainer<AboutFragment>(themeResId = R.style.App_DayNight)

        onView(withText(R.string.privacy_policy)).perform(click())

        verify { CustomTabs.openPrivacyPolicy(any()) }
    }

    @Test
    fun clickOnTermsOfService() {
        launchFragmentInContainer<AboutFragment>(themeResId = R.style.App_DayNight)

        onView(withText(R.string.terms_of_service)).perform(click())

        verify { CustomTabs.openTermsOfService(any()) }
    }

    @Test
    fun clickOnOpenSourceLicenses() {
        launchFragmentInContainer<AboutFragment>(themeResId = R.style.App_DayNight)
        Intents.init()

        onView(withText(R.string.open_source_licenses)).perform(click())

        intended(hasComponent(OssLicensesMenuActivity::class.java.name))
        Intents.release()
    }

    @After
    fun afterTests() = unmockkAll()
}
