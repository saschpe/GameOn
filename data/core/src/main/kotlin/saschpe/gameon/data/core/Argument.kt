package saschpe.gameon.data.core

sealed class Argument {
    object Void : Argument()

    data class Collection<Type>(val collection: Collection<Type>) : Argument()

    data class Page(val offset: Int, val count: Int) : Argument()

    data class Type<T>(val value: T) : Argument()
}