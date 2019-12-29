package saschpe.gameon.common

import saschpe.gameon.common.content.AppContentProvider.Companion.applicationContext

object Module {
    val colors: Colors by lazy { Colors(applicationContext) }
}