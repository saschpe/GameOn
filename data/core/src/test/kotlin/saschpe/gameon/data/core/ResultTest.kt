package saschpe.gameon.data.core

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.Test

class ResultTest {
    @Test
    fun resultSuccess_hasMatchingTypeAndData() {
        // Arrange, act
        val result = Result.Success("Hello")

        // Assert
        assertEquals("Hello", result.data)
    }

    @Test
    fun resultError_hasMatchingTypeAndData() {
        // Arrange, act
        val result = Result.Error(NullPointerException())

        // Assert
        assertEquals(NullPointerException::class, result.throwable::class)
        assertNull(result.throwable.localizedMessage)
    }

    @Test
    fun resultError_withMessage() {
        // Arrange, act
        val result = Result.Error.withMessage("Foo")

        // Assert
        assertEquals(Throwable::class, result.throwable::class)
        assertEquals("Foo", result.throwable.localizedMessage)
    }

    @Test
    fun resultError_withCause() {
        // Arrange
        val exception = NullPointerException("gah")

        // Act
        val result = Result.Error.withCause("raised", exception)

        // Assert
        assertEquals(Throwable::class, result.throwable::class)
        assertEquals("raised", result.throwable.localizedMessage)
        assertEquals(NullPointerException::class, result.throwable.cause!!::class)
        assertEquals("gah", result.throwable.cause!!.localizedMessage)
    }
}