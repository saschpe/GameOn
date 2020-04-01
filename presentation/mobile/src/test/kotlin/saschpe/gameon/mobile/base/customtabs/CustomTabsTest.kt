package saschpe.gameon.mobile.base.customtabs

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomTabsTest {
    private lateinit var context: Context

    @Before
    fun beforeMocks() = mockkObject(CustomTabs)

    @Before
    fun beforeContext() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun openHomepage_callsStartUrl() = runBlocking {
        // Arrange
        coEvery { CustomTabs.openUrl(context, any()) } just runs

        // Act
        CustomTabs.openHomepage(context)

        // Assert
        coVerify { CustomTabs.openUrl(context, any()) }
    }

    @Test
    fun openPrivacyPolicy_callsStartUrl() = runBlocking {
        // Arrange
        coEvery { CustomTabs.openUrl(context, any()) } just runs

        // Act
        CustomTabs.openPrivacyPolicy(context)

        // Assert
        coVerify { CustomTabs.openUrl(context, any()) }
    }

    @Test
    fun openTermsOfService_callsStartUrl() = runBlocking {
        // Arrange
        coEvery { CustomTabs.openUrl(context, any()) } just runs

        // Act
        CustomTabs.openTermsOfService(context)

        // Assert
        coVerify { CustomTabs.openUrl(context, any()) }
    }

    @After
    fun afterMocks() = unmockkObject(CustomTabs)
}
