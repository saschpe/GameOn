package saschpe.gameon.common.text

import android.text.Editable
import com.google.android.material.textfield.TextInputLayout
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TextInputLayoutDisableErrorTextWatcherTest {
    private val editable = mockk<Editable>()
    private val textInputLayout = mockk<TextInputLayout>(relaxed = true)
    private val textWatcher = spyk(
        TextInputLayoutDisableErrorTextWatcher(textInputLayout)
    )

    @Test
    fun afterTextChanged_callsNothing() {
        // Act
        textWatcher.afterTextChanged(editable)

        // Assert
        verify { textWatcher.afterTextChanged(editable) }
        verify { textInputLayout wasNot Called }
    }

    @Test
    fun beforeTextChanged_isErrorEnabledSetToFalse() {
        // Act
        textWatcher.beforeTextChanged(
            TEST_TEXT,
            TEST_START,
            TEST_BEFORE,
            TEST_COUNT
        )

        // Assert
        verifySequence {
            textWatcher.beforeTextChanged(
                TEST_TEXT,
                TEST_START,
                TEST_BEFORE,
                TEST_COUNT
            )
            textInputLayout.isErrorEnabled = false
        }
        assertEquals(false, textInputLayout.isErrorEnabled)
    }

    @Test
    fun onTextChanged_callsNothing() {
        // Act
        textWatcher.onTextChanged(
            TEST_TEXT,
            TEST_START,
            TEST_BEFORE,
            TEST_COUNT
        )

        // Assert
        verify {
            textWatcher.onTextChanged(
                TEST_TEXT,
                TEST_START,
                TEST_BEFORE,
                TEST_COUNT
            )
        }
        verify { textInputLayout wasNot Called }
    }

    companion object {
        private const val TEST_BEFORE = 1
        private const val TEST_COUNT = 2
        private const val TEST_START = 0
        private const val TEST_TEXT = "text"
    }
}
