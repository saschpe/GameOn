package saschpe.gamesale.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val id: String,
    val name: String,
    val color: String
)