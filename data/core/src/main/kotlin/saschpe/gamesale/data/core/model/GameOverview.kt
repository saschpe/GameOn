package saschpe.gamesale.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class GameOverview(
    val price: Price,
    val lowest: Lowest,
    val bundles: Bundles,
    val urls: Urls
) {
    @Serializable
    data class Price(
        val store: String,
        val cut: Int,
        val price: Float,
        val price_formatted: String,
        val url: String,
        val drm: List<String>
    )

    @Serializable
    data class Lowest(
        val store: String,
        val cut: Int,
        val price: Float,
        val price_formatted: String,
        // TODO: val url
        val recorded: Int,
        val recorded_formatted: String
    )

    @Serializable
    data class Bundles(
        val count: Int
        // TODO: val live
    )

    @Serializable
    data class Urls(
        val info: String,
        val history: String,
        val bundles: String
    )
}