package saschpe.gamesale.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Offer(
    val plain: String,
    val title: String,
    val price_new: Float,
    val price_old: Float,
    val price_cut: Float,
    val added: Long,
    val shop: Shop,
    val drm: Set<String>,
    val urls: Urls
) {
    @Serializable
    data class Shop(val id: String, val name: String)

    @Serializable
    data class Urls(val buy: String, val game: String)
}