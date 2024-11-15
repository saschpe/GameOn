package saschpe.gameon.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Store(val id: String, val name: String, val color: String)

const val STEAM_STORE = "steam"
