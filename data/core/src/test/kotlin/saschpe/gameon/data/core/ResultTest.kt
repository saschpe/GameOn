package saschpe.gameon.data.core

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.fail

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

    @Test
    fun asResult_withSuccess() = runBlocking {
        // Arrange
        suspend fun successfulFunction(): Result<String> = asResult { "Hello" }

        // Act, assert
        when (val result = successfulFunction()) {
            is Result.Success<String> -> assertEquals("Hello", result.data)
            is Result.Error -> fail()
        }
    }

    @Test
    fun asResult_withError() = runBlocking {
        // Arrange
        suspend fun throwingFunction(): Result<String> =
            asResult { throw ArithmeticException("Oops") }

        // Act, assert
        when (val result = throwingFunction()) {
            is Result.Success<String> -> fail()
            is Result.Error -> {
                assertEquals(ArithmeticException::class, result.throwable.cause!!::class)
                assertEquals("Oops", result.throwable.localizedMessage)
            }
        }
    }
}