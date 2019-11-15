package saschpe.gameon.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val code: String,
    val sign: String,
    val delimiter: String,
    val left: Boolean,
    val name: String,
    val html: String
)