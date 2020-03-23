package saschpe.gameon.mobile.base

import saschpe.gameon.data.core.Result
import saschpe.log4k.Log

fun Result.Error.errorLogged() = apply {
    Log.error(throwable.message.toString(), throwable)
}