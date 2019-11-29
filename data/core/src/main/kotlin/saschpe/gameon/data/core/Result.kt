package saschpe.gameon.data.core

/**
 * Wraps any computational result with compile-time type safety
 *
 * Suitable to catch and deliver exceptions across thread and process
 * boundaries (e.g. when an exception happened on a networking thread
 * and needs to be delivered to the UI thread). Works perfectly with
 * coroutines.
 */
sealed class Result<out T : Any> {
    /**
     * Generic success type.
     */
    data class Success<out T : Any>(val data: T) : Result<T>()

    /**
     * Generic error type.
     */
    data class Error(val throwable: Throwable) : Result<Nothing>() {
        companion object {
            /**
             * Convenience function to create an Error with a [Throwable] and message
             */
            fun withMessage(message: String) = Error(Throwable(message))

            /**
             * Wraps a [Throwable] raised elsewhere with an appropriate message
             *
             * Use if you want to, e.g., wrap a third-party library specific exception
             * with your own exception type to avoid third party code leaking into your
             * API interface.
             */
            fun withCause(message: String?, throwable: Throwable) =
                Error(Throwable(message, throwable))
        }
    }
}