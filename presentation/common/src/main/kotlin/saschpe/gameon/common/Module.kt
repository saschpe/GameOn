package saschpe.gameon.common

import saschpe.gameon.common.base.Colors
import saschpe.gameon.common.base.content.AppContentProvider.Companion.applicationContext

object Module {
    val colors: Colors by lazy {
        Colors(
            applicationContext
        )
    }
}