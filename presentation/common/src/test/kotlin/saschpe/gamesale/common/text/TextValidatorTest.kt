package saschpe.gamesale.common.text

import android.text.Editable
import io.mockk.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TextValidatorTest {
    internal class TestTextValidator : TextValidator() {
        override fun validate(text: String) = Unit
    }

    private val textValidator = spyk<TestTextValidator>()
    private val editable = mockk<Editable>()

    @Test
    fun validate_doesNothing() = textValidator.validate(TEST_TEXT)

    @Test
    fun afterTextChanged_callsValidate() {
        // Arrange
        every { editable.toString() } returns TEST_TEXT

        // Act
        textValidator.afterTextChanged(editable)

        // Assert
        verifySequence {
            textValidator.afterTextChanged(editable)
            textValidator.validate(TEST_TEXT)
        }
    }

    @Test
    fun beforeTextChanged_callsNothing() {
        // Act
        textValidator.beforeTextChanged(
            TEST_TEXT,
            TEST_START,
            TEST_BEFORE,
            TEST_COUNT
        )

        // Assert
        verify {
            textValidator.beforeTextChanged(
                TEST_TEXT,
                TEST_START,
                TEST_BEFORE,
                TEST_COUNT
            )
        }
    }

    @Test
    fun onTextChanged_callsNothing() {
        // Act
        textValidator.onTextChanged(
            TEST_TEXT,
            TEST_START,
            TEST_BEFORE,
            TEST_COUNT
        )

        // Assert
        verify {
            textValidator.onTextChanged(
                TEST_TEXT,
                TEST_START,
                TEST_BEFORE,
                TEST_COUNT
            )
        }
    }

    companion object {
        private const val TEST_BEFORE = 1
        private const val TEST_COUNT = 2
        private const val TEST_START = 0
        private const val TEST_TEXT = "text"
    }
}
