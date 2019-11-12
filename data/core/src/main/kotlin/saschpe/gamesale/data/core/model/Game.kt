package saschpe.gamesale.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val title: String,
    val image: String,
    val is_dlc: Boolean,
    val urls: Urls
) {
    @Serializable
    data class Urls(
        val game: String
    )
}