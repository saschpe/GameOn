package saschpe.gamesale.domain

sealed class Argument {
    object Void : Argument()

    data class Collection<T>(val collection: Collection<T>) : Argument()

    data class Page(val offset: Int, val count: Int) : Argument()

    data class Type<T>(val value: T) : Argument()
}

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()

    data class Error(val throwable: Throwable) : Result<Nothing>() {
        companion object {
            fun withMessage(message: String) = Error(Throwable(message))
            fun withCause(message: String?, throwable: Throwable) =
                Error(Throwable(message, throwable))
        }
    }

    class ResultException(message: String? = null, cause: Throwable? = null) :
        Throwable(message, cause)
}

interface UseCase<in A, out R> {
    suspend operator fun invoke(parameter: A? = null): R
}
