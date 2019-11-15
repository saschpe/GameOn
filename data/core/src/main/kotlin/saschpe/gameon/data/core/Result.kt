package saschpe.gameon.data.core

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()

    data class Error(val throwable: Throwable) : Result<Nothing>() {
        companion object {
            fun withMessage(message: String) = Error(Throwable(message))

            fun withCause(message: String?, throwable: Throwable) =
                Error(Throwable(message, throwable))
        }
    }
}